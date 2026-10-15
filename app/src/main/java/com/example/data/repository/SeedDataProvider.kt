package com.example.data.repository

import com.example.data.model.StationEntity
import com.example.data.model.TelemetryReadingEntity
import com.example.data.model.ThresholdConfig

object SeedDataProvider {

    fun getDefaultThresholds(): ThresholdConfig {
        return ThresholdConfig(
            id = 1,
            porePressureWarningKpa = 50.0,
            porePressureCriticalKpa = 75.0,
            tiltWarningDeg = 2.5,
            tiltCriticalDeg = 4.0,
            rainfall24hWarningMm = 80.0,
            rainfall24hCriticalMm = 120.0,
            seismicPgaWarningGal = 8.0,
            autoAlertsEnabled = true
        )
    }

    fun getInitialStations(): List<StationEntity> {
        return listOf(
            StationEntity(
                id = "loc-sk-01",
                code = "NER-SK-01",
                name = "Gangtok Ridge - Deorali Slopes",
                state = "Sikkim",
                district = "East Sikkim",
                latitude = 27.3256,
                longitude = 88.6122,
                elevationM = 1650.0,
                slopeAngleDeg = 42.5,
                soilType = "Silty Gneissic Colluvium",
                lithology = "Biotite Gneiss & Mica Schist",
                drainageDensity = "Very High",
                riskLevel = "CRITICAL",
                description = "Steep urbanized ridge with active slope creep above NH10 lifeline corridor.",
                populationAtRisk = 14500,
                criticalInfrastructure = "NH10 Highway, Deorali Ropeway Station, Power Substation",
                stationStatus = "CRITICAL",
                isBookmarked = true
            ),
            StationEntity(
                id = "loc-sk-02",
                code = "NER-SK-02",
                name = "Mangan - Chungthang Highway Sector",
                state = "Sikkim",
                district = "North Sikkim",
                latitude = 27.502,
                longitude = 88.528,
                elevationM = 1420.0,
                slopeAngleDeg = 48.0,
                soilType = "Coarse Debris & Phyllitic Silt",
                lithology = "Daling Quartzite & Pelitic Phyllite",
                drainageDensity = "Extreme",
                riskLevel = "CRITICAL",
                description = "Tectonically sheared slope near Main Central Thrust (MCT) with heavy debris slide history.",
                populationAtRisk = 5200,
                criticalInfrastructure = "North Sikkim Border Road, Teesta Hydro Project Intake",
                stationStatus = "CRITICAL",
                isBookmarked = true
            ),
            StationEntity(
                id = "loc-mg-01",
                code = "NER-MG-01",
                name = "Cherrapunji (Sohra) Escarpment",
                state = "Meghalaya",
                district = "East Khasi Hills",
                latitude = 25.2986,
                longitude = 91.7322,
                elevationM = 1430.0,
                slopeAngleDeg = 36.5,
                soilType = "Lateritic Red Sandy Silt",
                lithology = "Khasi Sandstone & Basalt",
                drainageDensity = "High",
                riskLevel = "HIGH",
                description = "High rainfall plateau boundary with intense monsoon pore-water accumulation.",
                populationAtRisk = 8800,
                criticalInfrastructure = "Shillong-Cherrapunji Tourist Corridor, Limestone Quarries",
                stationStatus = "WARNING",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-mg-02",
                code = "NER-MG-02",
                name = "Nongstoin - Mawkyrwat Ridge",
                state = "Meghalaya",
                district = "West Khasi Hills",
                latitude = 25.52,
                longitude = 91.27,
                elevationM = 1380.0,
                slopeAngleDeg = 31.0,
                soilType = "Clayey Gravel Colluvium",
                lithology = "Archean Gneissic Complex",
                drainageDensity = "Moderate",
                riskLevel = "MODERATE",
                description = "Agricultural slopes susceptible to saturation-induced rotational slips.",
                populationAtRisk = 4100,
                criticalInfrastructure = "State Highway 3, Regional Water Reservoir",
                stationStatus = "ONLINE",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-as-01",
                code = "NER-AS-01",
                name = "Dima Hasao - Haflong Hill Pass",
                state = "Assam",
                district = "Dima Hasao",
                latitude = 25.1667,
                longitude = 93.0167,
                elevationM = 850.0,
                slopeAngleDeg = 38.5,
                soilType = "Shale-dominated Weathered Clay",
                lithology = "Disang Group Sandstone-Shale",
                drainageDensity = "Very High",
                riskLevel = "CRITICAL",
                description = "Highly unstable valley corridor prone to track submergence and mudslides.",
                populationAtRisk = 12300,
                criticalInfrastructure = "Lumding-Badarpur Hill Section Railway, NH54E Bridge",
                stationStatus = "CRITICAL",
                isBookmarked = true
            ),
            StationEntity(
                id = "loc-as-02",
                code = "NER-AS-02",
                name = "Guwahati - Kamakhya Hills Sector",
                state = "Assam",
                district = "Kamrup Metropolitan",
                latitude = 26.166,
                longitude = 91.705,
                elevationM = 240.0,
                slopeAngleDeg = 34.0,
                soilType = "Red Sandy Loam with Clay Pockets",
                lithology = "Granitic Gneiss Basement",
                drainageDensity = "Moderate",
                riskLevel = "HIGH",
                description = "Dense hill settlement with artificial slope cutting and drainage blockage.",
                populationAtRisk = 22000,
                criticalInfrastructure = "Kamakhya Temple Access Road, Guwahati Water Supply Tank",
                stationStatus = "WARNING",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-ar-01",
                code = "NER-AR-01",
                name = "Itanagar - Papum Pare Slopes",
                state = "Arunachal Pradesh",
                district = "Papum Pare",
                latitude = 27.0844,
                longitude = 93.6053,
                elevationM = 560.0,
                slopeAngleDeg = 39.0,
                soilType = "Soft Siwalik Sandstone & Siltstone",
                lithology = "Sub-Himalayan Molasse",
                drainageDensity = "High",
                riskLevel = "HIGH",
                description = "Fragile Siwalik formation with rapid urban encroachment and gully erosion.",
                populationAtRisk = 9500,
                criticalInfrastructure = "Civil Secretariat Approach Road, Dikrong River Bridges",
                stationStatus = "WARNING",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-ar-02",
                code = "NER-AR-02",
                name = "Tawang - Sela Pass Highway",
                state = "Arunachal Pradesh",
                district = "Tawang",
                latitude = 27.505,
                longitude = 92.103,
                elevationM = 2750.0,
                slopeAngleDeg = 45.0,
                soilType = "Glacial Till & Weathered Schist",
                lithology = "Greater Himalayan Crystallines",
                drainageDensity = "High",
                riskLevel = "MODERATE",
                description = "Permafrost thaw and freeze-thaw cracking along strategic high-altitude road.",
                populationAtRisk = 3100,
                criticalInfrastructure = "Sela Tunnel Bypass, Military Logistics Route",
                stationStatus = "ONLINE",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-nl-01",
                code = "NER-NL-01",
                name = "Kohima - Zubza NH29 Bypass",
                state = "Nagaland",
                district = "Kohima",
                latitude = 25.6751,
                longitude = 94.1086,
                elevationM = 1440.0,
                slopeAngleDeg = 41.0,
                soilType = "Shattered Splintery Shale",
                lithology = "Disang Flysch Series",
                drainageDensity = "Very High",
                riskLevel = "HIGH",
                description = "Chronic sinkage zone on NH29 lifeline connecting Nagaland and Manipur.",
                populationAtRisk = 16800,
                criticalInfrastructure = "National Highway 29, Zubza New Rail Link Pier",
                stationStatus = "WARNING",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-mn-01",
                code = "NER-MN-01",
                name = "Noney - Tupul Railway Yard",
                state = "Manipur",
                district = "Noney",
                latitude = 24.81,
                longitude = 93.63,
                elevationM = 680.0,
                slopeAngleDeg = 44.0,
                soilType = "Weathered Mudstone & Siltstone",
                lithology = "Oligo-Miocene Surma Group",
                drainageDensity = "Very High",
                riskLevel = "CRITICAL",
                description = "Site of historical catastrophic debris flow near Ijai river valley.",
                populationAtRisk = 6700,
                criticalInfrastructure = "Jiribam-Imphal Railway Yard, Ijai River Culvert Bridge",
                stationStatus = "CRITICAL",
                isBookmarked = true
            ),
            StationEntity(
                id = "loc-mz-01",
                code = "NER-MZ-01",
                name = "Aizawl - Laipuitlang Crest",
                state = "Mizoram",
                district = "Aizawl",
                latitude = 23.7271,
                longitude = 92.7176,
                elevationM = 1130.0,
                slopeAngleDeg = 46.0,
                soilType = "Interbedded Siltstone & Clay Shale",
                lithology = "Surma-Bhuban Formation",
                drainageDensity = "Very High",
                riskLevel = "HIGH",
                description = "Steep topography with vulnerable high-density multi-storey buildings.",
                populationAtRisk = 28500,
                criticalInfrastructure = "World Bank Aizawl Bypass, Civil Hospital, Telecom Tower",
                stationStatus = "WARNING",
                isBookmarked = false
            ),
            StationEntity(
                id = "loc-tr-01",
                code = "NER-TR-01",
                name = "Jampui Hills - Vanghmun Ridge",
                state = "Tripura",
                district = "North Tripura",
                latitude = 23.95,
                longitude = 92.28,
                elevationM = 930.0,
                slopeAngleDeg = 28.0,
                soilType = "Sandy Clay Loam with Sandstone core",
                lithology = "Tipam Sandstone Formation",
                drainageDensity = "Moderate",
                riskLevel = "LOW",
                description = "Forested mountain range with localized bank erosion during heavy downpours.",
                populationAtRisk = 3400,
                criticalInfrastructure = "Inter-State Border Highway, Betlingshib Viewpoint",
                stationStatus = "ONLINE",
                isBookmarked = false
            )
        )
    }

    fun getInitialTelemetry(): List<TelemetryReadingEntity> {
        val list = mutableListOf<TelemetryReadingEntity>()

        // Helper to generate realistic trend sequences matching user telemetry
        fun addSequence(
            locId: String,
            rainRateStart: Double, rainRateEnd: Double,
            rainAccStart: Double, rainAccEnd: Double,
            poreStart: Double, poreEnd: Double,
            tiltStart: Double, tiltEnd: Double,
            seismicStart: Double, seismicEnd: Double,
            moistureStart: Double, moistureEnd: Double
        ) {
            val steps = 13
            val hours = listOf(
                "2026-08-30T18:31:07Z", "2026-08-30T19:31:07Z", "2026-08-30T20:31:07Z",
                "2026-08-30T21:31:07Z", "2026-08-30T22:31:07Z", "2026-08-30T23:31:07Z",
                "2026-08-31T00:31:07Z", "2026-08-31T01:31:07Z", "2026-08-31T02:31:07Z",
                "2026-08-31T03:31:07Z", "2026-08-31T04:31:07Z", "2026-08-31T05:31:07Z",
                "2026-08-31T06:31:07Z"
            )

            for (i in 0 until steps) {
                val factor = i.toDouble() / (steps - 1)
                val rate = roundOne(rainRateStart + (rainRateEnd - rainRateStart) * factor)
                val acc = roundOne(rainAccStart + (rainAccEnd - rainAccStart) * factor)
                val pore = roundOne(poreStart + (poreEnd - poreStart) * factor)
                val tilt = roundTwo(tiltStart + (tiltEnd - tiltStart) * factor)
                val pga = roundOne(seismicStart + (seismicEnd - seismicStart) * factor)
                val moist = roundOne(moistureStart + (moistureEnd - moistureStart) * factor)
                val batt = roundOne(99.0 - (factor * 1.2))
                val signal = if (i % 2 == 0) -65 else -67

                list.add(
                    TelemetryReadingEntity(
                        id = "sens-$locId-${12 - i}",
                        locationId = locId,
                        timestamp = hours[i],
                        rainfallRateMmHr = rate,
                        rainfallAcc24hMm = acc,
                        porePressureKpa = pore,
                        inclinometerTiltDeg = tilt,
                        seismicPgaGal = pga,
                        soilMoisturePct = moist,
                        batteryPct = batt,
                        signalStrengthDbm = signal
                    )
                )
            }
        }

        // loc-sk-01 Gangtok
        addSequence("loc-sk-01", 23.1, 38.5, 71.0, 142.0, 59.1, 84.5, 3.84, 4.80, 7.1, 14.2, 69.0, 92.0)
        // loc-sk-02 Mangan
        addSequence("loc-sk-02", 25.2, 42.0, 82.8, 165.5, 64.4, 92.0, 4.16, 5.20, 9.2, 18.5, 71.6, 95.5)
        // loc-mg-01 Cherrapunji
        addSequence("loc-mg-01", 16.8, 28.0, 97.5, 195.0, 49.7, 71.0, 2.08, 2.60, 3.4, 6.8, 66.0, 88.0)
        // loc-mg-02 Nongstoin
        addSequence("loc-mg-02", 5.1, 8.5, 22.5, 45.0, 22.4, 32.0, 0.72, 0.90, 1.1, 2.1, 46.5, 62.0)
        // loc-as-01 Dima Hasao
        addSequence("loc-as-01", 21.0, 35.0, 69.0, 138.0, 61.6, 88.0, 3.28, 4.10, 5.5, 11.0, 70.5, 94.0)
        // loc-as-02 Guwahati
        addSequence("loc-as-02", 13.2, 22.0, 46.0, 92.0, 44.8, 64.0, 1.84, 2.30, 2.2, 4.5, 60.8, 81.0)
        // loc-ar-01 Itanagar
        addSequence("loc-ar-01", 14.7, 24.5, 55.0, 110.0, 47.9, 68.5, 2.24, 2.80, 4.1, 8.2, 63.4, 84.5)
        // loc-ar-02 Tawang
        addSequence("loc-ar-02", 2.4, 4.0, 11.0, 22.0, 17.5, 25.0, 0.40, 0.50, 0.8, 1.5, 36.0, 48.0)
        // loc-nl-01 Kohima
        addSequence("loc-nl-01", 15.6, 26.0, 59.0, 118.0, 51.8, 74.0, 2.48, 3.10, 4.7, 9.4, 64.5, 86.0)
        // loc-mn-01 Noney
        addSequence("loc-mn-01", 23.4, 39.0, 77.5, 155.0, 62.3, 89.0, 3.68, 4.60, 8.0, 16.0, 70.1, 93.5)
        // loc-mz-01 Aizawl
        addSequence("loc-mz-01", 15.0, 25.0, 52.5, 105.0, 48.3, 69.0, 2.32, 2.90, 3.8, 7.5, 62.2, 83.0)
        // loc-tr-01 Jampui Hills
        addSequence("loc-tr-01", 3.2, 5.5, 14.0, 28.0, 18.2, 24.5, 0.45, 0.62, 0.9, 1.8, 38.0, 51.0)

        return list
    }

    private fun roundOne(v: Double): Double = Math.round(v * 10.0) / 10.0
    private fun roundTwo(v: Double): Double = Math.round(v * 100.0) / 100.0
}
