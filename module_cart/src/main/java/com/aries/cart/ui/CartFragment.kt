package com.aries.cart.ui

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.aries.common.base.BaseFragment
import com.aries.cart.R
import com.aries.cart.ui.view.QuickEntryPopup
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.common.util.StatusBarUtil
import com.aries.common.util.UnreadMsgUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.top_address.*

class CartFragment : BaseFragment(R.layout.fragment_cart), MavericksView {
    private val viewModel: CartViewModel by activityViewModel()
    private val maybeLikeListAdapter: GoodsListAdapter by lazy { GoodsListAdapter(arrayListOf())}
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initView() {
        initStatusBarPlaceholder()

        UnreadMsgUtil.show(threePointsBadgeNum, 2)

//        threePointsLayout.setOnClickListener {
//            showQuickEntry()
//        }

        contentLayout.run {
            setEnableRefresh(false)
            setEnableLoadMore(false)
        }

        //可能喜欢列表或者快来看看列表
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题
        maybeLikeListRecyclerView.run {
            adapter = maybeLikeListAdapter
            layoutManager = staggeredGridLayoutManager
            addItemDecoration(SpacesItemDecoration(10))
        }
    }

    override fun initData() {
        viewModel.initMaybeLikeList()
    }

    private fun initStatusBarPlaceholder() {
//        topAddressLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
//        val layoutParams = statusBarPlaceholder.layoutParams
//        layoutParams.height = StatusBarUtil.getHeight()
//        statusBarPlaceholder.layoutParams = layoutParams
    }

    //顶部快捷入口
    private fun showQuickEntry() {
        XPopup.Builder(this.requireContext())
            .popupAnimation(PopupAnimation.TranslateFromTop)
            .hasShadowBg(false)
            .isLightStatusBar(true)
            .asCustom(QuickEntryPopup(this.requireContext()))
            .show()
    }

    override fun invalidate() {
        withState(viewModel) {
            if (it.goodsList.isNotEmpty()) {
                maybeLikeListAdapter.setList(it.goodsList)
            }
        }
    }
}