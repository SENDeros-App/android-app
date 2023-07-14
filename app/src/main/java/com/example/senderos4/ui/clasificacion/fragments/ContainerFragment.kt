package com.example.senderos4.ui.clasificacion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.senderos4.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ContainerFragment : Fragment() {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_container, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()
        //addListener()


        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tabLayout.tabIconTint = null
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.icon_bronce_division)
                }

                1 -> {
                    tab.setIcon(R.drawable.icon_oro_division)
                }

                2 -> {
                    tab.setIcon(R.drawable.icon_rubi_division)
                }
            }
        }.attach()

    }


    companion object {
        private const val ARG_OBJECT = "object"
    }


    fun bind() {
        viewPager2 = requireView().findViewById(R.id.viewPager_Ranking)
        tabLayout = requireView().findViewById(R.id.motion_viewPager)

        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager2.adapter = demoCollectionAdapter
    }

    class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            val fragment = ClassificationsFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_OBJECT, position)
            }
            return fragment
        }


    }
}
