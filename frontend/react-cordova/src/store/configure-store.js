import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware, { END } from 'redux-saga'
import { persistState } from 'redux-devtools';
import rootReducer from 'src/reducers';
import DevTools from 'src/containers/dev-tools.jsx';

export default initialState => {
  const sagaMiddleware = createSagaMiddleware();
  const enhancer = compose(
    applyMiddleware(sagaMiddleware),
    // Required! Enable Redux DevTools with the monitors you chose
    DevTools.instrument(),
    // Optional. Lets you write ?debug_session=<key> in address bar to persist debug sessions
    persistState(__SERVER__ ? undefined : window.location.href.match(/[?&]debug_session=([^&]+)\b/))
  );
  // Note: only Redux >= 3.1.0 supports passing enhancer as third argument.
  // See https://github.com/rackt/redux/releases/tag/v3.1.0
  const store = createStore(rootReducer, initialState, __DEVTOOLS__ ? enhancer : applyMiddleware(sagaMiddleware));

  // Hot reload reducers (requires Webpack or Browserify HMR to be enabled)
  /* eslint-disable no-undef, global-require */
  if (module.hot) {
    module.hot.accept('../reducers', () =>
      store.replaceReducer(require('../reducers'))
    );
  }
  /* eslint-enable no-undef, global-require */

  store.runSaga = sagaMiddleware.run
  store.close = () => store.dispatch(END)

  return store;
};