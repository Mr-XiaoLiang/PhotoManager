import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // https://mvnrepository.com/artifact/org.json/json
            implementation("org.json:json:20250517")
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            // implementation("net.engawapg.lib:zoomable:2.8.1")
            implementation("io.github.panpf.zoomimage:zoomimage-compose-coil3:1.4.0-beta02")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.lollipop.photo.MainKt"
        buildTypes.release {
            proguard {
                configurationFiles.from(file("proguard-rules.pro"))
                isEnabled = false
            }
        }

        jvmArgs += listOf("-Xmx2G")
        val appName = "PhotoManager"
        val versionName = "1.0.0"
        val pkgName = "com.lollipop.photo"
        val sdf = SimpleDateFormat("yyyyMMdd-HHmmss")
        val buildVersion = "${versionName}-${sdf.format(Date(System.currentTimeMillis()))}"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appName
            packageVersion = versionName
            description = appName
            macOS {
                dockName = appName
                bundleID = pkgName
                pkgPackageVersion = versionName
                pkgPackageBuildVersion = buildVersion
//                iconFile.set(project.file("src/desktopMain/resources/icon.icns"))
            }
            windows {
                menuGroup = appName
                dirChooser = true
//                iconFile.set(project.file("src/desktopMain/resources/icon.ico"))
            }
            linux {
                packageName = appName
                menuGroup = appName
//                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
            }
        }

    }
}
