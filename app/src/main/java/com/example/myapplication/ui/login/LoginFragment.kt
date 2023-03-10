package com.example.myapplication.ui.login

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.R.color.*
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.ui.register.Validations
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val EMAIL_KEY = "email_key"
const val PASSWORD_KEY = "password_key"

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login)  {

    @Inject
    lateinit var validations: Validations

    private lateinit var mBinding :  FragmentLoginBinding
    private val viewModel : LoginViewModel by activityViewModels()
    private var isValidEmail= true
    private var isValidPassword= true

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
            inputEmail.doAfterTextChanged {
                isValidEmail = validations.isValidEmail(it.toString())
                mBinding.ilMail.error = if (isValidEmail) null else getString(R.string.error_mail)
                if(isValidEmail && isValidPassword) else activateButton(false)
            }
            inputPass.doAfterTextChanged {
                isValidPassword = validations.isValidPassword(it.toString())
                mBinding.ilPass.error = if (isValidPassword) null else getString(R.string.error_pass)
                if (isValidPassword && isValidEmail) activateButton(true) else activateButton(false)
            }
            bnLogin.setOnClickListener {
                viewModel.login(mBinding.inputEmail.text.toString(),mBinding.inputPass.text.toString())
            }
            bnRegister.setOnClickListener{
                val navBuilder = NavOptions.Builder()
                navBuilder.setEnterAnim(R.anim.enter_from_left).setExitAnim(R.anim.exit_from_left)
                    .setPopEnterAnim(R.anim.enter_from_right).setPopExitAnim(R.anim.exit_from_right)
                findNavController().navigate(R.id.action_loginFragment_to_stepOneRegisterFragment2, null, navBuilder.build())
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

    private fun activateButton(n: Boolean){
        if (n) {
            with(mBinding.bnLogin) {
                setBackgroundColor(resources.getColor(blueCoppel))
                isClickable = true
            }
        }else{
            with(mBinding.bnLogin) {
                setBackgroundColor(resources.getColor(grey4))
                isClickable = false
            }
        }
    }
}