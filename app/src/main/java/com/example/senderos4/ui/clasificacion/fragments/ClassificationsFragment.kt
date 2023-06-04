package com.example.senderos4.ui.clasificacion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.R
import com.example.senderos4.ui.clasificacion.adapter.ClassificationAdapter
import com.example.senderos4.ui.clasificacion.viewmodels.ClassificationViewModel

class ClassificationsFragment : HiddenMenuFragment() {

    private  val viewModel: ClassificationViewModel by viewModels{
        ClassificationViewModel.Factory
    }

    companion object{
        private const val ARG_OBJECT = "object"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            when(getInt(ARG_OBJECT)){
                0->{
                    view.findViewById<RecyclerView>(R.id.recyclerView).adapter = ClassificationAdapter().apply {
                        submitData(viewModel.getHeaders(), viewModel.getTopUsersByDivision("bronce", 17))
                        view.findViewById<ImageView>(R.id.division).setImageResource(R.drawable.division_bronce)
                    }

                }
                1->{
                    view.findViewById<RecyclerView>(R.id.recyclerView).adapter = ClassificationAdapter().apply {
                        submitData(viewModel.getHeaders(), viewModel.getTopUsersByDivision("oro", 17))
                        view.findViewById<ImageView>(R.id.division).setImageResource(R.drawable.division_oro)
                    }
                }
                2->{
                    view.findViewById<RecyclerView>(R.id.recyclerView).adapter = ClassificationAdapter().apply {
                        submitData(viewModel.getHeaders(), viewModel.getTopUsersByDivision("rubi", 17))
                        view.findViewById<ImageView>(R.id.division).setImageResource(R.drawable.division_rubi)
                    }
                }
            }
        }
        /*view.findViewById<RecyclerView>(R.id.recyclerView).adapter = ClassificationAdapter().apply {
            submitData(viewModel.getHeaders(), viewModel.getTop())
        }*/
    }

}