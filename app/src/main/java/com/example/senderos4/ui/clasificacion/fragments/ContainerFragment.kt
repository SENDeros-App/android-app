

package com.example.senderos4.ui.clasificacion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.senderos4.R
import com.example.senderos4.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.ui.clasificacion.adapter.ClassificationAdapter
import com.example.senderos4.ui.clasificacion.viewmodels.ClassificationViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ContainerFragment : HiddenMenuFragment() {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    //private lateinit var preButton: ImageView
    //private lateinit var nexButton:ImageView


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
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.liga_senderos)
                }1->{
                    tab.setIcon(R.drawable.icon_user_item)
                }
                2->{
                    tab.setIcon(R.drawable.info_settings)
                }
            }
        }.attach()
    }


    fun bind(){
        viewPager2 = requireView().findViewById(R.id.pager)
        tabLayout = requireView().findViewById(R.id.movimiento)
        //nexButton = requireView().findViewById(R.id.nextButton)
        //preButton = requireView().findViewById(R.id.prevButton)

        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager2.adapter = demoCollectionAdapter
    }

    /*fun addListener(){
        preButton.setOnClickListener {
            viewPager2.setCurrentItem(viewPager2.currentItem - 1, true)
        }

        nexButton.setOnClickListener {
            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
        }

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

                preButton.visibility = if(position == 0) {
                    View.GONE
                }
                else{
                    View.VISIBLE
                }

                nexButton.visibility = if(position == 2 ){
                    View.GONE
                }
                else{
                    View.VISIBLE
                }
            }
        })
    }*/

    companion object{
        private const val ARG_OBJECT = "object"
    }

    class DemoCollectionAdapter(fragment: Fragment):FragmentStateAdapter(fragment){
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            val fragment = ClassificationsFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_OBJECT, position)
            }
            return fragment
        }


    }


    /*class DemoCollectionAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->{
                    ClassificationsFragment()
                }
                1->{
                    SecondFragment()
                }
                2->{
                    Thirdragment()
                }
                else -> throw IllegalArgumentException("Error desconocido")
            }
        }

    }*/



}