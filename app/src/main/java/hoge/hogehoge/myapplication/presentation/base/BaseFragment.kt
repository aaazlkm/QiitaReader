package hoge.hogehoge.myapplication.presentation.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import dagger.android.support.DaggerFragment
import hoge.hogehoge.myapplication.R
import hoge.hogehoge.myapplication.presentation.NavigationController
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseFragment : DaggerFragment() {
    @Inject
    lateinit var navigationController: NavigationController

    val compositeDisposable = CompositeDisposable()

    val baseActivity: BaseActivity?
        get() = activity as? BaseActivity

    //region lifecycle

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val backStackEntryCount = fragmentManager?.backStackEntryCount ?: 0
                    if (backStackEntryCount <= 1) {
                        activity?.finish()
                    } else {
                        onBackPressed()
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        hideKeyboard()
        compositeDisposable.clear()
    }

    //endregion

    /**
     * バックキーが押下された際に呼ばれる
     * バックキーの制御をする場合このメソッドを継承する
     */
    open fun onBackPressed() {}

    fun hideKeyboard() {
        activity?.currentFocus?.let {
            val manager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    ?: return
            manager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun showToast(message: String) {
        baseActivity?.showToast(message)
    }

    fun showDialog(
        title: String,
        message: String,
        okText: String = getString(R.string.common_dialog_ok),
        doOnClickOk: (() -> Unit)? = null,
        cancelText: String = getString(R.string.common_dialog_cancel),
        doOnClickCancel: (() -> Unit)? = null
    ) {
        baseActivity?.showDialog(
            title,
            message,
            okText,
            doOnClickOk,
            cancelText,
            doOnClickCancel
        )
    }

    fun setLocadingView(needShow: Boolean) {
        baseActivity?.setLoadingView(needShow)
    }
}
