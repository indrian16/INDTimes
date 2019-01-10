package io.indrian16.indtimes.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.indrian16.indtimes.R

class FavoriteFragment : androidx.fragment.app.Fragment() {

    companion object {

        fun newInstance() = FavoriteFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

}
