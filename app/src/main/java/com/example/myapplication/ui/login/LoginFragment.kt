package com.example.myapplication.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val EMAIL_KEY = "email_key"

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login)  {

    private lateinit var mBinding :  FragmentLoginBinding
    private val viewModel : LoginViewModel by activityViewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(layoutInflater, container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        subscribeLiveData()
    }

    private fun setListeners() {
        with(mBinding) {
            bnLogin.setOnClickListener {
                viewModel.login(mBinding.inputEmail.text.toString(),mBinding.inputPass.text.toString())
                Toast.makeText(mBinding.root.context, "Hola", Toast.LENGTH_SHORT).show()
            }
            bnRegister.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.userExist.observe(viewLifecycleOwner){
            if (it) {
                sharedPreferences.edit().putString(EMAIL_KEY, mBinding.inputEmail.text.toString()).apply()
                findNavController().navigate(R.id.action_loginFragment_to_assistenceMainFragment)
            }
            else context?.toast(getString(R.string.user_not_exist))
        }
    }

}