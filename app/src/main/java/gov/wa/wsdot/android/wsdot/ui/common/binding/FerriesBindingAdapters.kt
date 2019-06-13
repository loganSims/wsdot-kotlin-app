package gov.wa.wsdot.android.wsdot.ui.common.binding

import android.R
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import gov.wa.wsdot.android.wsdot.db.ferries.TerminalCombo
import gov.wa.wsdot.android.wsdot.db.ferries.Vessel
import gov.wa.wsdot.android.wsdot.ui.ferries.route.TerminalComboAdapter
import gov.wa.wsdot.android.wsdot.util.network.Resource
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data Binding adapters specific to the app.
 */
object FerriesBindingAdapters {

    // Ferries Binding Adapters


    @JvmStatic
    @BindingAdapter("bindVesselName")
    fun bindVesselName(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            textView.text = vessel.data.vesselName
            return
        }
        textView.text = "Vessel Name Unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselSchDeparture")
    fun bindVesselSchDeparture(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            if (vessel.data.scheduledDeparture != null) {
                textView.text = BindingAdapters.getHourString(vessel.data.scheduledDeparture)
                return
            }
        }
        textView.text = "unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselActualDeparture")
    fun bindVesselActualDeparture(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            if (vessel.data.leftDock != null) {
                textView.text = BindingAdapters.getHourString(vessel.data.leftDock)
                return
            }
        }
        textView.text = "unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselEta")
    fun bindVesselEta(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            if (vessel.data.eta != null) {
                textView.text = BindingAdapters.getHourString(vessel.data.eta)
                return
            }
        }
        textView.text = "unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselLocation")
    fun bindVesselLocation(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            textView.text = String.format("%f, %f", vessel.data.latitude, vessel.data.longitude)
            return
        }
        textView.text = "unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselHeading")
    fun bindVesselHeading(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            textView.text = String.format("%.1f", vessel.data.heading)
            return
        }
        textView.text = "unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselSpeed")
    fun bindVesselSpeed(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            textView.text = String.format("%.1f", vessel.data.speed)
            return
        }
        textView.text = "unavailable"
    }

    @JvmStatic
    @BindingAdapter("bindVesselWebpage")
    fun bindVesselWebpage(textView: TextView, vessel: Resource<Vessel>) {
        if (vessel.data != null) {
            textView.text = String.format("http://www.wsdot.com/ferries/vesselwatch/VesselDetail.aspx?vessel_id=%s", vessel.data.vesselId)
            return
        }
        textView.text = "unavailable"
    }


    @JvmStatic
    @BindingAdapter(value = ["terminalCombos", "selectedTerminalCombo", "selectedTerminalComboAttrChanged"], requireAll = false)
    fun setTerminalCombos(spinner: Spinner, terminal: Resource<List<TerminalCombo>>, selectedTerminal: TerminalCombo?, listener: InverseBindingListener) {
        if (terminal.data == null) return
        spinner.adapter = TerminalComboAdapter(spinner.context, R.layout.simple_spinner_dropdown_item, terminal.data)

        setCurrentSelection(spinner, selectedTerminal)
        setSpinnerListener(spinner, listener)

    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedTerminalCombo", event = "selectedTerminalComboAttrChanged")
    fun getSelectedTerminalCombo(spinner: Spinner): TerminalCombo {
        Log.e("debug", "setting terminal combo in VM")
        return spinner.selectedItem as TerminalCombo
    }

    // Ferries spinner helpers
    private fun setSpinnerListener(spinner: Spinner, listener: InverseBindingListener) {

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            var lastPosition = spinner.selectedItemPosition

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // do nothing if same item is selected, this prevents infinite loops caused
                // by the 2-way binding.
                // https://medium.com/androiddevelopers/android-data-binding-2-way-your-way-ccac20f6313
                if (lastPosition != position) {
                    lastPosition = position
                    listener.onChange()
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
        }
    }

    private fun setCurrentSelection(spinner: Spinner, selectedItem: TerminalCombo?) {
        if (selectedItem == null) {
            return
        }
        for (index in 0 until spinner.adapter.count) {
            val currentItem = spinner.getItemAtPosition(index) as TerminalCombo
            if ( (currentItem.departingTerminalId == selectedItem.departingTerminalId)
                && (currentItem.arrivingTerminalId == selectedItem.arrivingTerminalId)) {
                spinner.setSelection(index)
                return
            }
        }

        spinner.setSelection(0)

    }



}
