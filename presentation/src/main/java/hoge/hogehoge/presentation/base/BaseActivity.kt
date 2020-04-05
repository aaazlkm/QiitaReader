package hoge.hogehoge.presentation.base

import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import hoge.hogehoge.presentation.NavigationController
import hoge.hogehoge.presentation.R
import javax.inject.Inject

open class BaseActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var navigationController: NavigationController

    // TODO
    // 現在の実装だと目に見えない形でR.id.loadingViewに依存しいるので、うまくない
    // 他にいい実装を思いついたら修正すること
    private val loadingView: View by lazy {
        findViewById<View>(R.id.loadingView)
    }

    //region Activity override methods

    override fun onSupportNavigateUp(): Boolean {
        navigationController.popFragment()
        return super.onSupportNavigateUp()
    }

    //endregion

    fun setupActionBar(title: String) {
        val canback = supportFragmentManager.backStackEntryCount > 1
        supportActionBar?.setDisplayHomeAsUpEnabled(canback)
        supportActionBar?.title = title
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showDialog(
        title: String,
        message: String,
        positiveText: String = getString(R.string.common_dialog_ok),
        doOnClickPositive: (() -> Unit)? = null,
        negativeText: String = getString(R.string.common_dialog_cancel),
        doOnClickNegative: (() -> Unit)? = null

    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ ->
                doOnClickPositive?.invoke()
            }
            .setNegativeButton(negativeText) { _, _ ->
                doOnClickNegative?.invoke()
            }
            .show()
    }

    fun setLoadingView(needShow: Boolean) {
        loadingView.visibility = if (needShow) View.VISIBLE else View.GONE
    }
}
