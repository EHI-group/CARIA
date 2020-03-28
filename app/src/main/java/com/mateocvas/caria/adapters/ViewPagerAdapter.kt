package com.mateocvas.caria.adapters




import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mateocvas.caria.ui.bascket.BascketFragment
import com.mateocvas.caria.ui.others.OthersFragment
import com.mateocvas.caria.ui.register.RegisterFragment
import com.mateocvas.caria.ui.shoop.ShoopFragment


class ViewPagerAdapter(manager: FragmentManager,val array:ArrayList<Fragment>) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> ShoopFragment()
            1-> BascketFragment()
            2-> RegisterFragment()
            else-> OthersFragment()
        }
    }

    override fun getCount(): Int {
    return array.size
    }

}






