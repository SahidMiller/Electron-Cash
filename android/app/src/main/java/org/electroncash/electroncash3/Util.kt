package org.electroncash.electroncash3

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast


val UNIT_BCH = 100000000L
val UNIT_MBCH = 100000L
var unitSize = UNIT_BCH  // TODO: make unit configurable
var unitName = "BCH"     //

fun toSatoshis(s: String, unit: Long = unitSize) : Long? {
    try {
        return Math.round(s.toDouble() * unit)
    } catch (e: NumberFormatException) { return null }
}

fun formatSatoshis(amount: Long, unit: Long = unitSize): String {
    val places = Math.log10(unit.toDouble()).toInt()
    return "%.${places}f".format(amount.toDouble() / unit)
}


fun showDialog(activity: FragmentActivity, frag: DialogFragment) {
    val fm = activity.supportFragmentManager
    val tag = frag.javaClass.simpleName
    if (fm.findFragmentByTag(tag) == null) {
        frag.show(fm, tag)
    }
}

fun dismissDialog(activity: FragmentActivity, simpleName: String) {
    val frag = activity.supportFragmentManager.findFragmentByTag(simpleName)
    (frag as DialogFragment?)?.dismiss()
}


// Since error messages are likely to be surprising, set the default duration to long.
class ToastException(message: String, val duration: Int = Toast.LENGTH_LONG)
    : Exception(message) {

    constructor(resId: Int, duration: Int = Toast.LENGTH_LONG)
        : this(App.context.getString(resId), duration)

    fun show() { toast(message!!, duration) }
}

fun toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.context, text, duration).show()
}
fun toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(App.context.getString(resId), duration)
}


// Based on https://medium.com/google-developers/android-data-binding-recyclerview-db7c40d9f0e4
abstract class BoundAdapter(val layoutId: Int)
    : RecyclerView.Adapter<BoundViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoundViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, viewType, parent, false)
        return BoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoundViewHolder, position: Int) {
        holder.binding.setVariable(BR.model, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected abstract fun getItem(position: Int): Any
}

class BoundViewHolder(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root)
