package com.example

import com.example.data.model.RiskLevel
import com.example.data.model.StationWithLatestTelemetry
import com.example.data.repository.SeedDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SlopeWatchGeotechnicalUnitTest {

    @Test
    fun seedData_containsTwelveStations() {
        val stations = SeedDataProvider.getInitialStations()
        assertEquals(12, stations.size)
        assertTrue(stations.any { it.code == "NER-SK-01" })
        assertTrue(stations.any { it.code == "NER-MN-01" })
    }

    @Test
    fun seedData_containsTelemetryReadings() {
        val telemetry = SeedDataProvider.getInitialTelemetry()
        assertTrue(telemetry.isNotEmpty())
        val gangtokReadings = telemetry.filter { it.locationId == "loc-sk-01" }
        assertEquals(13, gangtokReadings.size)
    }

    @Test
    fun factorOfSafety_criticalWhenHighTiltAndPorePressure() {
        val station = SeedDataProvider.getInitialStations().first { it.id == "loc-sk-01" }
        val latestReading = SeedDataProvider.getInitialTelemetry().findLast { it.locationId == "loc-sk-01" }
        assertNotNull(latestReading)

        val model = StationWithLatestTelemetry(
            station = station,
            latestReading = latestReading
        )

        // For steep slope 42.5 deg with 84.5 kPa pore pressure and 4.8 deg tilt
        val fos = model.estimatedFactorOfSafety
        assertTrue("FoS should indicate critical failure condition (<= 1.1)", fos <= 1.1)
        assertEquals(RiskLevel.CRITICAL, model.evaluatedRiskLevel)
    }

    @Test
    fun riskLevel_evaluationMatchesThresholds() {
        assertEquals(RiskLevel.CRITICAL, RiskLevel.fromString("CRITICAL"))
        assertEquals(RiskLevel.HIGH, RiskLevel.fromString("HIGH"))
        assertEquals(RiskLevel.MODERATE, RiskLevel.fromString("MODERATE"))
        assertEquals(RiskLevel.LOW, RiskLevel.fromString("LOW"))
    }
}
