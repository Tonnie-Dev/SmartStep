package com.tonyxlab.smartstep.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val customMaterialShapes = Shapes(
        // Pre-Defined M3 Shapes
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),

        // Extra-Large Override
        extraLarge = RoundedCornerShape(20.dp)
)

object ExtendedShapes {
    val RoundedCornerShape4 = RoundedCornerShape(4.dp)
    val RoundedCornerShape10 = RoundedCornerShape(10.dp)
    val RoundedCornerShape14 = RoundedCornerShape(14.dp)
    val RoundedCornerShape24 = RoundedCornerShape(24.dp)
    val RoundedCornerShape28 = RoundedCornerShape(28.dp)

    val StartVerticalRoundedCornerShape100 =
        RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp)
    val EndVerticalRoundedCornerShape100 =
        RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp)

    val HorizontalRoundedCornerShape28 = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    /*
        val HorizontalRoundedCornerShape16 = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

        val HorizontalRoundedCornerShape24 = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)



        val VerticalRoundedCornerShape16 = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)

        val TopLeftShape16 = RoundedCornerShape(topStart = 16.dp)*/
}

val Shapes.RoundedCornerShape4
    @Composable
    get() = ExtendedShapes.RoundedCornerShape4

val Shapes.RoundedCornerShape10
    @Composable
    get() = ExtendedShapes.RoundedCornerShape10

val Shapes.RoundedCornerShape14
    @Composable
    get() = ExtendedShapes.RoundedCornerShape14

val Shapes.RoundedCornerShape24
    @Composable
    get() = ExtendedShapes.RoundedCornerShape24

val Shapes.RoundedCornerShape28
    @Composable
    get() = ExtendedShapes.RoundedCornerShape28

val Shapes.StartVerticalRoundedCornerShape100
    @Composable
    get() = ExtendedShapes.StartVerticalRoundedCornerShape100

val Shapes.EndVerticalRoundedCornerShape100
    @Composable
    get() = ExtendedShapes.EndVerticalRoundedCornerShape100

val Shapes.HorizontalRoundedCornerShape28
    @Composable
    get() = ExtendedShapes.HorizontalRoundedCornerShape28

