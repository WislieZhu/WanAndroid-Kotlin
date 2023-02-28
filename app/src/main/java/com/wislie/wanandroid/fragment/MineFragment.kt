package com.wislie.wanandroid.fragment

import android.Manifest
import android.app.Activity
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.common.util.CameraUtil
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentMineBinding
import com.wislie.wanandroid.ext.loadImage
import com.wislie.wanandroid.ext.requestPermission
import com.wislie.wanandroid.ext.startDestination
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.viewmodel.CoinViewModel
import com.wislie.wanandroid.viewmodel.LoginViewModel
import com.wislie.wanandroid.viewmodel.MineStateViewModel


/**
 * 我的 todo 如果没有登录到登录, 还会不会调用 loadData
 */
class MineFragment : BaseViewModelFragment<MineStateViewModel, FragmentMineBinding>() {

    private val coinViewModel: CoinViewModel by viewModels()

    private val logoutViewModel: LoginViewModel by viewModels()

    //拍照
    private val takeCameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Settings.avatar?.run {
                binding.ivCamera.loadImage(this)
            }
        }
    }

    //从相册中选取
    private val pickFromAlbumResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.data?.data?.run {
            val path = CameraUtil.getPath2uri(hostActivity, this)
            path?.run {
                Settings.avatar = this
                binding.ivCamera.loadImage(this)
            }
        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_mine
    }

    override fun init(root: View) {
        super.init(root)
        binding.mineViewModel = mViewModel
        binding.btnScore.setOnClickListener { //积分

            startDestination {
                findNav().navigate(R.id.fragment_coin_rank)
            }
        }

        binding.btnCollect.setOnClickListener { //我的收藏
            startDestination {
                findNav().navigate(R.id.fragment_collect)
            }
        }

        binding.btnUrls.setOnClickListener {  //常用网站
            findNav().navigate(R.id.fragment_usual_website)
        }

        binding.btnTodo.setOnClickListener { //todo列表
            startDestination {
                findNav().navigate(R.id.fragment_todo_list)
            }
//            log()
        }


        binding.btnMyArticle.setOnClickListener { //我分享的文章
            startDestination {
                findNav().navigate(R.id.fragment_share_private_article_list)
            }
        }

        binding.btnMyPic.setOnClickListener {  //头像
            takeCamera()
        }

        binding.btnLogout.setOnClickListener { //退出登录
//            logoutViewModel.logout()
            pickFromAlbum()
        }


    }

    //修改每一个item, 点击author跳转, asm点击优化, 搜索按钮,
    // 我的页面， tab字体大小和下划线太近, 拍照, 清除图标太大, 加载中一闪而过, Bundle传值
    // 启动慢
    private fun takeCamera() {

        requestPermission(
            hostActivity,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            startDestination {
                CameraUtil.takeCamera(hostActivity) { path ->
                    Settings.avatar = path
                }.run {
                    takeCameraResultLauncher.launch(this)
                }
            }
        }
    }

    private fun pickFromAlbum() {
        requestPermission(
            hostActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            startDestination {
                CameraUtil.selectFromAlbum().run {
                    pickFromAlbumResultLauncher.launch(this)
                }
            }
        }
    }


    private fun log() {
        val path = Environment.getDataDirectory().absolutePath
        Log.i("wislieZhu", "path=$path")
        try {
            Thread.sleep(8000) //Application Not Responding for at least 5000 ms
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        /*BlockCanaryInternals.getLogFiles()?.run {
            this.forEach {
                Log.i("wislieZhu", " BlockCanaryLogFiles=${it.name}")
            }
        }*/
    }

    override fun loadData() { //表示已登录
        if (Settings.logined) {
            coinViewModel.getCoin()
        }
        //如果没有登录, 积分显示为--, 当前排名显示为--, 用户名显示为--
        //如果登录了, 积分显示为 coinCount, 当前排名显示为 rank, 用户名显示为username

        //现在的需求是 1.从未登录->登录, 显示发生变化;  2.从登录->未登录, 显示发生变化

        //1.从未登录->登录, 如何感知到登录了, 而且是全局感知


    }

    override fun observeData() {
        coinViewModel.coinResultLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { coin ->
                    coin?.run {
                        mViewModel?.coin?.set(this)
                    }
                })
            }

        App.instance()
            .appViewModel
            .userInfoLiveData
            .observe(viewLifecycleOwner) { userInfo ->
                userInfo?.run {
                    coinViewModel.getCoin()
                }
            }

        logoutViewModel.logoutResultLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                Settings.logined = false
                App.instance().appViewModel.userInfoLiveData.value = null
                mViewModel?.coin?.set(null)
            })
        }


    }
}