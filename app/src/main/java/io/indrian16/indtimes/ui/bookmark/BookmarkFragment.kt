package io.indrian16.indtimes.ui.bookmark

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.indrian16.indtimes.R

class BookmarkFragment : Fragment() {

    companion object {

        fun newInstance() = BookmarkFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

}
