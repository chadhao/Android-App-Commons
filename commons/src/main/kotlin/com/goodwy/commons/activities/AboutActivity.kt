package com.goodwy.commons.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goodwy.commons.R
import com.goodwy.commons.compose.extensions.config
import com.goodwy.commons.compose.extensions.enableEdgeToEdgeSimple
import com.goodwy.commons.compose.screens.AboutNewSection
import com.goodwy.commons.compose.screens.AboutScreen
import com.goodwy.commons.compose.theme.AppThemeSurface
import com.goodwy.commons.extensions.*
import com.goodwy.commons.helpers.*
import com.goodwy.commons.models.FAQItem

class AboutActivity : BaseComposeActivity() {
    private val appName get() = intent.getStringExtra(APP_NAME) ?: ""

    private var firstVersionClickTS = 0L
    private var clicksSinceFirstClick = 0

    companion object {
        private const val EASTER_EGG_TIME_LIMIT = 8000L
        private const val EASTER_EGG_REQUIRED_CLICKS = 7
        private const val EASTER_EGG_REQUIRED_CLICKS_NEXT = 10
    }

    fun getFlavorName() = intent.getStringExtra(APP_FLAVOR_NAME) ?: "gplay"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            val isTopAppBarColorIcon by config.isTopAppBarColorIcon.collectAsStateWithLifecycle(initialValue = config.topAppBarColorIcon)
            val isTopAppBarColorTitle by config.isTopAppBarColorTitle.collectAsStateWithLifecycle(initialValue = config.topAppBarColorTitle)
            AppThemeSurface {
                AboutScreen(
                    goBack = ::finish,
                    aboutSection = {
                        AboutNewSection(
                            appName = appName,
                            appVersion = intent.getStringExtra(APP_VERSION_NAME) ?: "",
                            appFlavor = getFlavorName(),
                            packageName = packageName,
                            onFAQClick = ::launchFAQActivity,
                            onLicenseClick = ::onLicenseClick,
                            onContributorsClick = ::onContributorsClick,
                            onVersionClick = ::onVersionClick,
                        )
                    },
                    isTopAppBarColorIcon = isTopAppBarColorIcon,
                    isTopAppBarColorTitle = isTopAppBarColorTitle,
                )
            }
        }
    }

    private fun launchFAQActivity() {
        val faqItems = intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem>
        Intent(applicationContext, FAQActivity::class.java).apply {
            putExtra(
                APP_ICON_IDS,
                intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList<String>()
            )
            putExtra(APP_LAUNCHER_NAME, intent.getStringExtra(APP_LAUNCHER_NAME) ?: "")
            putExtra(APP_FAQ, faqItems)
            startActivity(this)
        }
    }

    private fun onContributorsClick() {
        val intent = Intent(applicationContext, ContributorsActivity::class.java)
        startActivity(intent)
    }

    private fun onLicenseClick() {
        Intent(applicationContext, LicenseActivity::class.java).apply {
            putExtra(
                APP_ICON_IDS,
                intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList<String>()
            )
            putExtra(APP_LAUNCHER_NAME, intent.getStringExtra(APP_LAUNCHER_NAME) ?: "")
            putExtra(APP_LICENSES, intent.getLongExtra(APP_LICENSES, 0))
            startActivity(this)
        }
    }

    private fun onVersionClick() {
        if (firstVersionClickTS == 0L) {
            firstVersionClickTS = System.currentTimeMillis()
            Handler(Looper.getMainLooper()).postDelayed({
                firstVersionClickTS = 0L
                clicksSinceFirstClick = 0
            }, EASTER_EGG_TIME_LIMIT)
        }

        clicksSinceFirstClick++
        if (clicksSinceFirstClick == EASTER_EGG_REQUIRED_CLICKS) {
            toast(R.string.hello)
        } else if (clicksSinceFirstClick >= EASTER_EGG_REQUIRED_CLICKS_NEXT) {
            val appVersion = intent.getStringExtra(APP_VERSION_NAME) ?: ""
            toast("Version: $appVersion")
            firstVersionClickTS = 0L
            clicksSinceFirstClick = 0
        }
    }
}