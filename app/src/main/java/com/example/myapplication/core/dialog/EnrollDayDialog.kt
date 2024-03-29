package com.example.myapplication.core.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.data.models.Day
import com.example.myapplication.databinding.DialogEnrollToDayBinding
import com.example.myapplication.ui.home.HomeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Suppress("UNREACHABLE_CODE")
class EnrollToDayDialog(
    var isAdd: Boolean,
    var selectDay: Day,
    var email: String,
    var isConfirm: (Boolean) -> Unit): DialogFragment(R.layout.dialog_enroll_to_day) {

    private lateinit var mBinding: DialogEnrollToDayBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialogForm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogEnrollToDayBinding.inflate(layoutInflater, container, false)
        setView()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.backGroundRefused)))
        return mBinding.root
    }

    private fun setView() {
        if(isAdd){
            with(mBinding){
                btBack.setOnClickListener {
                    isConfirm(false)
                    dismiss()
                }
                btConfirm.setOnClickListener {
                    addOrDelete(true)
                    mBinding.cvMain.visibility=View.GONE
                    mBinding.cvMain2.visibility=View.VISIBLE
                    isConfirm(true)
                }
                btBack2.setOnClickListener {
                    isConfirm(false)
                    dismiss()
                }
            }
        }else{
            setUndoView()
            with(mBinding){
                btBack.setOnClickListener {
                    isConfirm(false)
                    dismiss()
                }
                btConfirm.setOnClickListener {
                    addOrDelete(false)
                    mBinding.cvMain.visibility=View.GONE
                    mBinding.cvMain2.visibility=View.VISIBLE
                    isConfirm(true)
                }
                btBack2.setOnClickListener {
                    isConfirm(false)
                    dismiss()
                }
            }
        }
    }

    private fun setUndoView() {
        with(mBinding){
            ivIconAdd.setImageResource(R.drawable.img_5)
            tvTitle.text = getString(R.string.text_confirm_delete)
            tvMessage.text=getString(R.string.tv_messagges_delete)
            tvMessage.visibility = View.GONE
            tvTitle2.text = getString(R.string.confirme_delete)
            ivIcon2.setImageResource(R.drawable.img_6)
        }
    }

    private fun addOrDelete(b: Boolean){
        viewModel.getListEmails(selectDay.date)
        if(b) viewModel.addUserToListUsers(email) else viewModel.deleteUserOfDay(email)
        viewModel.addUserToDay()
    }

}