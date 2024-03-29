package kr.co.express9.client.mvvm.view.fragment

import android.content.Intent
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentProfileBinding
import kr.co.express9.client.mvvm.model.data.CartHistory
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.view.CartHistoryActivity
import kr.co.express9.client.mvvm.view.LoginActivity
import kr.co.express9.client.mvvm.view.NotificationSettingActivity
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.extension.launchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val userViewModel: UserViewModel by viewModel()

    override fun initStartView(isRestart: Boolean) {
        val user = User.getUser()
        dataBinding.tvName.text = user.name
        dataBinding.llCartMemoHistory.setOnClickListener {
            activity?.launchActivity<CartHistoryActivity>()

        }

        dataBinding.llNotificationSetting.setOnClickListener {
            activity?.launchActivity<NotificationSettingActivity>()
        }

        dataBinding.llInquire.setOnClickListener { sendEmail() }
        user.image?.let {
            Glide.with(this)
                .load(it)
                .error(R.drawable.ic_account_64_px)
                .into(dataBinding.ivProfile)
        }

        dataBinding.llLogout.setOnClickListener { userViewModel.logout() }

        userViewModel.event.observe(this, Observer { event ->
            when (event) {
                UserViewModel.Event.LOGOUT -> activity!!.launchActivity<LoginActivity> {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
        })
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        val csvEmail = getString(R.string.csv_email)

        try {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(csvEmail))

            emailIntent.type = "text/html"
            emailIntent.setPackage("com.google.android.gm")
            if (emailIntent.resolveActivity(activity!!.packageManager) != null)
                startActivity(emailIntent)

            startActivity(emailIntent)
        } catch (e: Exception) {
            e.printStackTrace()

            emailIntent.type = "text/html"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(csvEmail))

            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }
    }
}