package com.example.movieappazi.ui.actors_screen

import androidx.lifecycle.viewModelScope
import com.example.data.network.cloud.base.ResourceProvider
import com.example.domain.assistant.DispatchersProvider
import com.example.domain.base.BaseMapper
import com.example.domain.domainModels.person.PersonsDomain
import com.example.domain.domainRepositories.network.person.PersonRepositories
import com.example.movieappazi.base.BaseViewModel
import com.example.movieappazi.extensions.changeResponseState
import com.example.movieappazi.uiModels.movie.ResponseState
import com.example.movieappazi.uiModels.person.PersonUi
import com.example.movieappazi.uiModels.person.PersonsUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ActorsFragmentViewModel @Inject constructor(
    private val personRepository: PersonRepositories,
    private val mapPersonResponseFromDomain: BaseMapper<PersonsDomain, PersonsUi>,
    private val resourceProvider: ResourceProvider,
    private val dispatchersProvider: DispatchersProvider,
) : BaseViewModel() {

    private val _error = MutableSharedFlow<String>(replay = 0)
    val error get() = _error.asSharedFlow()
    private val personResponsePage = MutableStateFlow(1)

    private val _personResponseState = MutableStateFlow(ResponseState())
    val personResponseState get() = _personResponseState.asStateFlow()

    fun goPersonDetails(person: PersonUi) {
        navigate(ActorsFragmentDirections.actionNavPersonToActorsDetailsFragment(person))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val persons = personResponsePage.flatMapLatest {
        personRepository.getPersons(personResponsePage.value)
    }.map(mapPersonResponseFromDomain::map).flowOn(dispatchersProvider.default())
        .catch { throwable: Throwable ->
            _error.emit(resourceProvider.handleException(throwable = throwable))
        }.onEach {
            _personResponseState.emit(changeResponseState(page = it.page,
                totalPage = it.total_pages))
        }.shareIn(viewModelScope, SharingStarted.Lazily, 1)


}