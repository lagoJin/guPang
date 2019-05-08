package kr.co.express9.client.mvvm.view.fragment

import com.bumptech.glide.Glide
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentProfileBinding
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.util.setImageUrl

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun initStartView(isRestart: Boolean) {
        val user = User.getUser()
        dataBinding.tvName.text = user.name
        user.image?.let {
            Glide.with(this)
                .load(it)
                .error(R.drawable.ic_account_64_px)
                .into(dataBinding.ivProfile)
        }
    }
}