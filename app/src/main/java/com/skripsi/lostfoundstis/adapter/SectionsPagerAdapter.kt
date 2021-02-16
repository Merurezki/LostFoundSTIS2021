package com.skripsi.lostfoundstis.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.skripsi.lostfoundstis.*

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@Suppress("DEPRECATION")
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun getItem(position: Int): Fragment {
        // Returning the current tabs
        return when(position){
            0 -> Pencarian()
            1 -> Penemuan()
            else -> Pencarian()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabTitles[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}