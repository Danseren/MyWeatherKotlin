package lesson12

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import geekbrains.android.myweatherkotlin.databinding.FragmentCitiesListBinding
import geekbrains.android.myweatherkotlin.databinding.FragmentVersionCodeBinding
import geekbrains.android.myweatherkotlin.view.view.contentprovider.ContentProviderFragment
import kotlinx.android.synthetic.main.fragment_version_code.*

class VersionCodeFragment : Fragment() {

    companion object {
        fun newInstance() = ContentProviderFragment()
    }

    private var _binding: FragmentVersionCodeBinding? = null
    private val binding: FragmentVersionCodeBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVersionCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnVersionAction = btnVersionAction.setOnClickListener {
            tvMessage.let {
                it.text = "PRO version code works"
                it.textSize = 36F
                it.setTextColor(android.graphics.Color.GREEN)
                it.gravity = android.view.Gravity.LEFT
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}