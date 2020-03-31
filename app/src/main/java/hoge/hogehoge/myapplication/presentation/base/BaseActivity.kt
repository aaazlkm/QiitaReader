package hoge.hogehoge.myapplication.presentation.base

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import dagger.android.support.DaggerAppCompatActivity
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.presentation.NavigationController
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
        AlertDialog.Builder(this)
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
