package com.example.myapplication.ui.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.models.Day
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentAssistencceWeekBinding
import com.example.myapplication.ui.home.adapters.UserAdapter
import com.example.myapplication.ui.home.adapters.WeekAdapter
import java.time.LocalDate

class AssistenceWeekFragment : Fragment(R.layout.fragment_assistencce_week) {

    private lateinit var mBinding : FragmentAssistencceWeekBinding
    private lateinit var mAdapter: WeekAdapter
    private lateinit var mUserAdapter: UserAdapter
    val weekDays: ArrayList<Day> = arrayListOf(Day(selected = true), Day(),Day(),Day(),Day())
    var day: Day = Day()
    @RequiresApi(Build.VERSION_CODES.O)
    var localDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding= FragmentAssistencceWeekBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            day = AssistenceWeekFragmentArgs.fromBundle(it).day
        }
        setListeners()
        //subscribeLiveData()
        setDaysAdapter()
        setUserAdapter()
    }

    private fun setDaysAdapter() {
        mAdapter = WeekAdapter(weekDays){
            click(it)
        }
        mBinding.recyclerWeek.apply {
            layoutManager= GridLayoutManager(mBinding.root.context,5)
            adapter=mAdapter
        }
    }
    private fun setUserAdapter() {
        mUserAdapter = UserAdapter(user= listOf(User(), User(), User(), User(), User(), User(), User(), User(), User(), User()) )
        mBinding.recyclerUsers.apply {
            layoutManager= LinearLayoutManager(activity?.applicationContext)
            adapter=mUserAdapter
        }
    }
    private fun subscribeLiveData() {
        TODO("Not yet implemented")
    }

    private fun setListeners() {
        with(mBinding){
            ivBack.setOnClickListener{
                activity?.onBackPressed()
            }
        }
    }
    private fun click(day:Day){

    }
}