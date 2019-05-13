package gov.wa.wsdot.android.wsdot.ui.ferries

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import gov.wa.wsdot.android.wsdot.repository.FerriesRepository
import javax.inject.Inject


// Uses Saved State module for ViewModels: https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate#kotlin

class FerriesViewModel @Inject constructor(
    ferriesRepository: FerriesRepository
) : ViewModel() {

    val routes : LiveData<String> = ferriesRepository.getSchedule()

}