package com.one2one.test.punk.core.plataform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.one2one.test.punk.R

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    internal fun showErrorSnackbar(text: String){
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red_300))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white)).show()
    }

    internal fun showErrorSnackbarBtn(text: String, btnLabel: String, btn: (View) -> Unit ){
        Snackbar.make(requireView(), text, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red_300))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setAction(btnLabel) {
                btn(it)
            }.show()
    }

}