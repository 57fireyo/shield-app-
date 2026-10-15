package com.example.data.model

data class StationWithLatestTelemetry(
    val station: StationEntity,
    val latestReading: TelemetryReadingEntity?,
    val historicalReadingsCount: Int = 0
) {
    /**
     * Geological Factor of Safety (FoS) heuristic estimation:
     * FoS > 1.3: Stable
     * 1.0 <= FoS <= 1.3: Marginally Stable / Warning
     * FoS < 1.0: Active Failure Imminent / Critical
     */
    val estimatedFactorOfSafety: Double
        get() {
            val tilt = latestReading?.inclinometerTiltDeg ?: 1.0
            val poreP = latestReading?.porePressureKpa ?: 20.0
            val slope = station.slopeAngleDeg

            // Mohr-Coulomb slope stability approximation for Himalayan colluvium
            val frictionAngleDeg = 32.0
            val effectiveNormalStress = 120.0 - poreP.coerceAtLeast(0.0) * 0.8
            val shearStrength = 25.0 + effectiveNormalStress.coerceAtLeast(5.0) * Math.tan(Math.toRadians(frictionAngleDeg))
            val drivingStress = 140.0 * Math.sin(Math.toRadians(slope)) * (1.0 + (tilt / 10.0))
            val fos = shearStrength / drivingStress.coerceAtLeast(1.0)
            return (Math.round(fos * 100.0) / 100.0).coerceIn(0.45, 2.50)
        }

    val evaluatedRiskLevel: RiskLevel
        get() {
            val reading = latestReading ?: return RiskLevel.fromString(station.riskLevel)
            return when {
                reading.inclinometerTiltDeg >= 4.0 || reading.porePressureKpa >= 75.0 || reading.rainfallAcc24hMm >= 130.0 -> RiskLevel.CRITICAL
                reading.inclinometerTiltDeg >= 2.3 || reading.porePressureKpa >= 50.0 || reading.rainfallAcc24hMm >= 75.0 -> RiskLevel.HIGH
                reading.inclinometerTiltDeg >= 1.0 || reading.porePressureKpa >= 30.0 || reading.rainfallAcc24hMm >= 35.0 -> RiskLevel.MODERATE
                else -> RiskLevel.LOW
            }
        }
}
