package com.example.senderos4.ui.clasificacion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.R
import com.example.senderos4.data.Header
import com.example.senderos4.data.User
import com.example.senderos4.ui.clasificacion.adapter.ClassificationAdapter
import com.example.senderos4.ui.clasificacion.viewmodels.ClassificationViewModel

class ClassificationsFragment : HiddenMenuFragment() {

    private lateinit var imageDivision:ImageView
    private lateinit var textTittleDivision:TextView
    private lateinit var texDescriptionDivision:TextView
    private lateinit var RecyclerViewRanking: RecyclerView
    private lateinit var nestedScrollView:NestedScrollView

    private val viewModel: ClassificationViewModel by viewModels {
        ClassificationViewModel.Factory
    }

    companion object {
        private const val ARG_OBJECT = "object"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classification, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {

            val league = when (getInt(ARG_OBJECT)) {
                0 -> {

                    League(
                        R.string.division_bronze,
                        R.string.division_bronze_description,
                        R.drawable.icon_bronce_division,
                        viewModel.getHeaders(),
                        viewModel.getTopUsersByDivision("bronce", 17)
                    )
                }
                1 -> {
                    League(
                        R.string.division_oro,
                        R.string.division_oro_description,
                        R.drawable.icon_oro_division,
                        viewModel.getHeaders(),
                        viewModel.getTopUsersByDivision("oro", 17)
                    )
                }
                2 -> {
                    League(
                        R.string.division_rubi,
                        R.string.division_rubi_description,
                        R.drawable.icon_rubi_division,
                        viewModel.getHeaders(),
                        viewModel.getTopUsersByDivision("rubi", 17)
                    )
                }
                else -> {
                    throw Exception()
                }
            }
            displayLeague(view, league)
        }

    }

    fun bind(){
        imageDivision = requireView().findViewById(R.id.imageDivision)
        textTittleDivision = requireView().findViewById(R.id.textTitleDivision)
        texDescriptionDivision = requireView().findViewById(R.id.TextDescriptionDivision)
        RecyclerViewRanking = requireView().findViewById(R.id.recyclerView)
        nestedScrollView = requireView().findViewById(R.id.scrollView)
    }

    private fun displayLeague(view: View, league: League) {
        bind()
        with(view) {
            RecyclerViewRanking.adapter = ClassificationAdapter().apply {
                submitData(league.header, league.user)
            }
            imageDivision.setImageResource(league.icon)
            textTittleDivision.text = getString(league.title)
            texDescriptionDivision.text = getString(league.description)
        }

    }

    private data class League(
        val title: Int,
        val description: Int,
        val icon: Int,
        val header: List<Header>,
        val user: List<User>
    )

    override fun onResume() {
        super.onResume()
        nestedScrollView.post {
            nestedScrollView.smoothScrollTo(0,0)
        }
    }
}