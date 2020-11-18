package com.one2one.test.punk.presentation.beer

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import coil.load
import com.google.android.material.transition.MaterialContainerTransform
import com.one2one.test.punk.R
import com.one2one.test.punk.core.extensions.themeColor
import com.one2one.test.punk.core.plataform.BaseFragment
import com.one2one.test.punk.domain.models.Beer
import kotlinx.android.synthetic.main.fragment_details_beers.*


class BeersDetailsFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_details_beers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(
                R.integer.reply_motion_duration_large
            ).toLong()
            containerColor = Color.BLACK
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beer: Beer? = arguments?.getSerializable("dataLabel") as Beer?

        beer?.let {
            imageViewBeer?.load(it.imageUrl)
            textViewName.text = it.name
            textViewDescription.text = it.description
            textViewDegree.text = "${requireActivity().getString(R.string.fragment_beers_details_label_degrees)}: ${it.degree}"
        }

        imageViewBack.setOnClickListener {
            NavHostFragment.findNavController(
                this@BeersDetailsFragment).popBackStack()
        }
    }
}