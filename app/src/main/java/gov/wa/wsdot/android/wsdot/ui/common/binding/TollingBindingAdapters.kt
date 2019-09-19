package gov.wa.wsdot.android.wsdot.ui.common.binding

import android.R
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import gov.wa.wsdot.android.wsdot.db.tollrates.dynamic.TollTrip
import gov.wa.wsdot.android.wsdot.ui.common.SpinnerStringPairAdapter

object TollingBindingAdapters {

    // Regular Binding Adapters
    @JvmStatic
    @BindingAdapter("bindTollTrip")
    fun bindTollTrip(textView: TextView, tollTrip: TollTrip?) {
        tollTrip?.let {
            textView.text = String.format("to %s", it.endLocationName)
        }
    }

    @JvmStatic
    @BindingAdapter("showTrip")
    fun showTrip(view: View, tollTrip: TollTrip?) {
        view.visibility = View.GONE
        tollTrip?.let {
            view.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("bindTripCost")
    fun bindTripCost(view: TextView, tollTrip: TollTrip?) {

        tollTrip?.let {

            view.text = String.format("$%.2f", it.currentRate)
            view.setTextColor(Color.WHITE)

        }
    }

    @JvmStatic
    @BindingAdapter("bindTripColor")
    fun bindTripColor(view: CardView, tollTrip: TollTrip?) {
        view.setBackgroundColor(Color.BLACK)
    }




}