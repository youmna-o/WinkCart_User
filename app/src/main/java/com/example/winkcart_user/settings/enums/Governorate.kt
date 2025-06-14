package com.example.winkcart_user.settings.enums

import androidx.annotation.StringRes
import com.example.winkcart_user.R

enum class Governorate(@StringRes val stringResId: Int) {
    CAIRO(R.string.governorate_cairo),
    ALEXANDRIA(R.string.governorate_alexandria),
    GIZA(R.string.governorate_giza),
    PORT_SAID(R.string.governorate_port_said),
    SUEZ(R.string.governorate_suez),
    LUXOR(R.string.governorate_luxor),
    ASWAN(R.string.governorate_aswan),
    ISMAILIA(R.string.governorate_ismailia),
    RED_SEA(R.string.governorate_red_sea),
    NEW_VALLEY(R.string.governorate_new_valley),
    MATRUH(R.string.governorate_matruh),
    SOUTH_SINAI(R.string.governorate_south_sinai),
    NORTH_SINAI(R.string.governorate_north_sinai),
    FAIYUM(R.string.governorate_faiyum),
    BENI_SUEF(R.string.governorate_beni_suef),
    MINYA(R.string.governorate_minya),
    ASYUT(R.string.governorate_asyut),
    SOHAG(R.string.governorate_sohag),
    QENA(R.string.governorate_qena),
    DAMIETTA(R.string.governorate_damietta),
    SHARQIA(R.string.governorate_sharqia),
    DAKAHLIA(R.string.governorate_dakahlia),
    KAFR_EL_SHEIKH(R.string.governorate_kafr_el_sheikh),
    BEHEIRA(R.string.governorate_beheira),
    MONUFIA(R.string.governorate_monufia),
    GHARBIA(R.string.governorate_gharbia);

    companion object {
        fun getAll(): List<Governorate> = entries
    }
}
