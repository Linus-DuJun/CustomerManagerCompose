package org.linus.du.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.linus.du.R

private val AppleFonts = FontFamily(
    Font(R.font.apple_light, FontWeight.W300),
    Font(R.font.apple_regular, FontWeight.W400),
    Font(R.font.apple_medium, FontWeight.W500),
    Font(R.font.apple_semibold, FontWeight.W600),
    Font(R.font.apple_bold, FontWeight.Bold),
    Font(R.font.apple_heavy, FontWeight.ExtraBold)
)

private val Montserrat = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_medium, FontWeight.W500),
    Font(R.font.montserrat_semibold, FontWeight.W600)
)

private val Domine = FontFamily(
    Font(R.font.domine_regular),
    Font(R.font.domine_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    h4 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp
    ),
    h5 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = AppleFonts,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = Domine,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)


















