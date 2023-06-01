package com.example.senderos4.ui.clasificacion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.senderos4.R
import com.example.senderos4.hiddenMenu.HiddenMenuFragment


class ContainerFragment : HiddenMenuFragment() {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager2: ViewPager2

    class DemoCollectionAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
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

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        demoCollectionAdapter = DemoCollectionAdapter(this)
        bind()
        viewPager2.adapter = demoCollectionAdapter
    }

    fun bind(){
        viewPager2 = requireView().findViewById(R.id.pager)
    }

}