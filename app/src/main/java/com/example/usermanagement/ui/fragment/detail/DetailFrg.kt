package com.example.usermanagement.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.usermanagement.data.entity.User
import com.example.usermanagement.databinding.FrgDetailBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a single detail screen.
 * This fragment is either contained in a [MasterActivity]
 * in two-pane mode (on tablets) or a [DetailActivity]
 * on handsets.
 */
@AndroidEntryPoint
class DetailFrg : Fragment() {

    companion object {
        const val ARG_USER  = "ARG_USER"

        fun newInstance(user: User?) = DetailFrg().apply {
            user?.let {
                arguments = Bundle().apply { putParcelable(ARG_USER, it) }
            }
        }
    }

    private val _detailFrgVM: DetailFrgVM by viewModels()
    private val _user by lazy { arguments?.getParcelable<User>(ARG_USER) }
    private var _binding: FrgDetailBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FrgDetailBinding.inflate(inflater, container, false)
        _binding!!.user = _user
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}