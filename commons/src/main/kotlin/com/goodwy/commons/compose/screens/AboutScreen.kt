package com.goodwy.commons.compose.screens

import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.goodwy.commons.R
import com.goodwy.commons.compose.extensions.MyDevices
import com.goodwy.commons.compose.lists.SimpleColumnScaffold
import com.goodwy.commons.compose.theme.AppThemeSurface
import com.goodwy.commons.compose.theme.SimpleTheme
import com.goodwy.commons.helpers.FontHelper
import java.util.Calendar
import com.goodwy.strings.R as stringsR

@Composable
internal fun AboutScreen(
    goBack: () -> Unit,
    aboutSection: @Composable () -> Unit,
    isTopAppBarColorIcon: Boolean,
    isTopAppBarColorTitle: Boolean,
) {
    SimpleColumnScaffold(
        title = stringResource(id = R.string.about),
        goBack = goBack,
        isTopAppBarColorIcon = isTopAppBarColorIcon,
        isTopAppBarColorTitle = isTopAppBarColorTitle,
    ) {
        aboutSection()
    }
}

@MyDevices
@Composable
private fun AboutScreenPreview() {
    AppThemeSurface {
        AboutScreen(
            goBack = {},
            aboutSection = {
                AboutNewSection(
                    setupFAQ = true,
                    appName = "Common",
                    appVersion = "1.0",
                    appFlavor = "foss",
                    packageName = "com.goodwy.common",
                    onFAQClick = {},
                    onLicenseClick = {},
                    onContributorsClick = {},
                    onVersionClick = {},
                )
            },
            isTopAppBarColorIcon = true,
            isTopAppBarColorTitle = true,
        )
    }
}

@Composable
internal fun AboutNewSection(
    setupFAQ: Boolean = true,
    appName: String,
    appVersion: String,
    appFlavor: String,
    packageName: String,
    onFAQClick: () -> Unit,
    onLicenseClick: () -> Unit,
    onContributorsClick: () -> Unit,
    onVersionClick: () -> Unit,
) {
    Box(
        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.about_margin))
    ) {
        val textColor = MaterialTheme.colorScheme.onSurface
        Column(Modifier.padding(start = 6.dp, end = 6.dp, bottom = 26.dp)) {
            Card(shape = RoundedCornerShape(16.dp)) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onVersionClick),
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .width(72.dp)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(68.dp)
                                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(34.dp), clip = true),
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = null,
                            )
                            Icon(
                                modifier = Modifier
                                    .size(72.dp),
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = appName,
                                tint = Color.Unspecified
                            )
                        }
                    },
                    headlineContent = {
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = appName,
                                fontSize = 18.sp,
                            )
                            Text(
                                text = "Version: $appVersion",
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                color = textColor.copy(alpha = 0.5F),
                            )
                            Text(
                                text = packageName,
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                color = textColor.copy(alpha = 0.5F),
                            )
                        }
                    },
                )
            }
            if (setupFAQ) {
                Spacer(modifier = Modifier.size(18.dp))
                AboutItem(
                    text = stringResource(R.string.frequently_asked_questions),
                    imageVector = Icons.Rounded.QuestionMark,
                    onClick = onFAQClick,
                )
            }
            Spacer(modifier = Modifier.size(18.dp))
            AboutItem(
                text = stringResource(stringsR.string.participants_title),
                imageVector = Icons.Rounded.Diversity3,
                onClick = onContributorsClick,
            )
            Spacer(modifier = Modifier.size(18.dp))
            AboutItem(
                text = stringResource(R.string.third_party_licences),
                imageVector = Icons.AutoMirrored.Outlined.Article,
                onClick = onLicenseClick,
            )
            Spacer(modifier = Modifier.size(20.dp))
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            Text(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.copyright_new, currentYear),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun AboutItem(
    modifierIcon: Modifier = Modifier
        .size(42.dp)
        .padding(8.dp),
    cardColor: Color = MaterialTheme.colorScheme.surface,
    text: String,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        val colorFor = contentColorFor(cardColor)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .weight(1f),
                text = text.toUpperCase(LocaleList.current),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = colorFor,
            )
            Box(
                modifier = Modifier
                    .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                    .width(42.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .alpha(0.2f)
                        .size(42.dp),
                    imageVector = Icons.Rounded.Circle,
                    contentDescription = text,
                    tint = colorFor
                )
                if (imageVector != null) Icon(
                    modifier = Modifier
                        .size(42.dp)
                        .padding(8.dp),
                    imageVector = imageVector,
                    contentDescription = text,
                    tint = colorFor
                )
                if (painter != null) Icon(
                    modifier = modifierIcon,
                    painter = painter,
                    contentDescription = text,
                    tint = colorFor
                )
            }
        }
    }
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier, textColor: Color = Color.Unspecified) {
    AndroidView(
        modifier = modifier.padding(horizontal = 4.dp),
        factory = { context -> TextView(context).apply {
            setTextColor(textColor.toArgb())
            typeface = FontHelper.getTypeface(context)
        } },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}