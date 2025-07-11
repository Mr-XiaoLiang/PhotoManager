package com.lollipop.photo.data

import com.lollipop.photo.values.StringsKey

enum class ContentDensityMode(
    val label: StringsKey
) {

    Less3(StringsKey.DensityLess3),
    Less2(StringsKey.DensityLess2),
    Less1(StringsKey.DensityLess1),
    Medium(StringsKey.DensityMedium),
    More1(StringsKey.DensityMore1),
    More2(StringsKey.DensityMore2),
    More3(StringsKey.DensityMore3),

}