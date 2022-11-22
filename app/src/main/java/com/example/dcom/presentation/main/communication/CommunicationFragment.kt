package com.example.dcom.presentation.main.communication

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.extension.hide
import com.example.dcom.extension.show
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.widget.CustomEditText

class CommunicationFragment: BaseFragment(R.layout.communication_fragment) {

    private lateinit var btnKeyBoard: ImageButton
    private lateinit var cedtInput: CustomEditText
    private lateinit var rvConversation: RecyclerView
    private lateinit var llBottomBar: LinearLayout
    private lateinit var ivBottomPlaceHolder: ImageView

    private val communicationAdapter by lazy {
        CommunicationAdapter()
    }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()

        // fake data
        communicationAdapter.addData(mockConversationData())
    }

    private fun setUpVariables() {
        btnKeyBoard = requireView().findViewById(R.id.btnCommunicationKeyboard)
        cedtInput = requireView().findViewById(R.id.cedtCommunicationInput)
        rvConversation = requireView().findViewById(R.id.rvCommunicationConversation)
        llBottomBar = requireView().findViewById(R.id.llCommunicationBottomBar)
        ivBottomPlaceHolder = requireView().findViewById(R.id.ivCommunicationBottomPlaceHolder)
    }

    private fun setUpOnClick() {
        btnKeyBoard.setOnClickListener {
            showInputBox()
        }
    }

    private fun setUpRecyclerView() {
        rvConversation.layoutManager = LinearLayoutManager(requireContext())
        setAdapterListener()
        rvConversation.adapter = communicationAdapter
    }

    private fun setAdapterListener() {
        communicationAdapter.listener = object : CommunicationAdapter.IListener {
            override fun onClickItem(index: Int) {
                hideInputBox()
            }
        }
    }

    private fun showInputBox() {
        llBottomBar.hide()
        cedtInput.show()
        ivBottomPlaceHolder.updateLayoutParams {
            height = cedtInput.height
        }
    }

    private fun hideInputBox() {
        hideKeyboard()
        cedtInput.hide()
        llBottomBar.show()
        ivBottomPlaceHolder.updateLayoutParams {
            height = llBottomBar.height
        }
    }

    private fun scrollRecyclerViewToLastItem() {
        rvConversation.postDelayed({
            rvConversation.scrollToPosition(rvConversation.adapter!!.itemCount - 1)
        }, 1000)
    }

    private fun mockConversationData(): MutableList<Any> {
        val ans = mutableListOf<Any>()

        val list = mutableListOf(
            getString(R.string.speech_to_text_des),
            getString(R.string.emergency_signal_des),
            getString(R.string.text_to_speech_des),
            getString(R.string.fast_communication_des),
            getString(R.string.example_paragraph),
            getString(R.string.example_paragraph2),
        )

        val listNumber = listOf(0, 0, 0, 0, 1)
        var count = 0

        for (i in 0 until 20) {
            if (i == 0 || listNumber.random() == 1) {
                ans.add(CommunicationAdapter.TimeDateData().apply {
                    timeDate = "11:28 AM $count"
                })
                count++
            }
            ans.add(CommunicationAdapter.MessageData().apply {
                message = list.random()
            })
        }
        return ans
    }

    private fun changeInputBoxHeight() {
        cedtInput.updateLayoutParams {
            height = 100
        }
    }

    private fun hideKeyboard() {
        ViewCompat.getWindowInsetsController(requireView())?.hide(WindowInsetsCompat.Type.ime())
    }
}
