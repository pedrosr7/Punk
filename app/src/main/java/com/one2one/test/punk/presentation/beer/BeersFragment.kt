package com.one2one.test.punk.presentation.beer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialElevationScale
import com.one2one.test.punk.R
import com.one2one.test.punk.core.extensions.*
import com.one2one.test.punk.core.plataform.BaseFragment
import com.one2one.test.punk.domain.models.Beer
import com.one2one.test.punk.domain.models.DomainError
import com.one2one.test.punk.presentation.beer.viewmodel.BeersViewModel
import kotlinx.android.synthetic.main.fragment_beers.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import thevoid.whichbinds.dslist.listDSL

@ExperimentalCoroutinesApi
class BeersFragment : BaseFragment() {

    private val beersViewModel: BeersViewModel by viewModel()

    override fun layoutId(): Int = R.layout.fragment_beers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(beersViewModel.showLoading) { show ->
            show?.let {
                val alpha = if (it) 1.0f else  0.0f

                progressbar_main.animate()
                    .alpha(alpha)
                    .withStartAction { if (it) progressbar_main?.let { view -> view.visibility = View.VISIBLE } }
                    .withEndAction { if (!it) progressbar_main?.let { view -> view.visibility = View.GONE } }
                    .duration = 1000
            }
        }

        observe(beersViewModel.showErrorObserver) { throwable ->
            throwable?.let { showErrors(it) }
        }

        textFile_search.afterTextChanged {
            if(it.isNotBlank()){
                beersViewModel.getBeers(it)
            }
        }

        setUpListBeer()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        beersViewModel.getBeers(null)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpListBeer() {
        recyclerViewBeer.layoutManager = GridLayoutManager(requireContext(),3)
        listDSL<Int, Beer> {

            init {
                recyclerView = this@BeersFragment.recyclerViewBeer
                shimmerViewId = R.layout.item_vertical_shimmer
                shimmersToAdd = 20
            }

            observe(beersViewModel.observerBeers){ beers ->

                ul {
                    beers?.forEach {
                        li {
                            id = it.id
                            content = it
                            viewType = R.layout.item_beer
                            viewBind { id, value, itemView, _ ->
                                val image: ImageView? =
                                        itemView.findViewById(R.id.imageView)
                                val name: TextView? =
                                        itemView.findViewById(R.id.textViewName)
                                val cardView: MaterialCardView? =
                                        itemView.findViewById(R.id.cardViewBeer)

                                image?.load(value?.imageUrl)
                                name?.text = value?.name
                                cardView?.transitionName = "${getString(R.string.cardView_transition_name)}_${value?.id}"

                                val cardViewTransitionName = getString(
                                    R.string.cardView_transition_name
                                )

                                cardView?.setOnClickListener {

                                    exitTransition = MaterialElevationScale(false).apply {
                                        duration = resources.getInteger(
                                            R.integer.reply_motion_duration_large
                                        ).toLong()
                                    }
                                    reenterTransition = MaterialElevationScale(true).apply {
                                        duration = resources.getInteger(
                                            R.integer.reply_motion_duration_large
                                        ).toLong()
                                    }

                                    val bundle = bundleOf("dataLabel" to value)
                                    val extras = FragmentNavigatorExtras(cardView to cardViewTransitionName)
                                    findNavController().navigate(
                                        R.id.action_beerFragment_to_beerDetailsFragment,
                                        bundle, null, extras
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun showErrors(throwable: Throwable){
        when (DomainError.fromThrowable(throwable)) {
            is DomainError.NotFoundError -> showErrorSnackbarBtn(getString(R.string.error_text_notfounderror), getString(R.string.snackbar_text_btn)) { beersViewModel.getBeers(null) }
            is DomainError.UnknownServerError -> showErrorSnackbarBtn(getString(R.string.error_text_unknownservererror), getString(R.string.snackbar_text_btn)) { beersViewModel.getBeers(null) }
            is DomainError.AuthenticationError -> showErrorSnackbarBtn(getString(R.string.error_text_authenticationerror), getString(R.string.snackbar_text_btn)) { beersViewModel.getBeers(null) }
            is DomainError.NetworkConnectionError -> showErrorSnackbarBtn(getString(R.string.error_text_nointernetconnection), getString(R.string.snackbar_text_btn)) { beersViewModel.getBeers(null) }
        }
    }

}