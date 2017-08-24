import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as CredentialsActions from 'src/actions/credentials-actions.js';
import auth from 'src/common/auth.js';

let Socket = require('sockjs-client');
let Stomp = require('stompjs');
let socket;
let stompClient;
const header = {'X-Auth-Token': token};

class TalkAppComponent extends Component {
  handleLogout = this.handleLogout.bind(this);
  handleLogout() {
    auth.logout();
    this.props.credentialsActions.clearCredentials();
  }

  componentDidMount() {
    const socketUrl = "http://localhost:8888/websocket";
    const stompConnect = () => {
      socket = new Socket(socketUrl);
      stompClient = Stomp.over(socket);
      stompClient.debug = null;
      stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        const url = '/talk/room.list';
        stompClient.subscribe(url, function (response) {
          // const url = 'http://' + config.apiHost + ':' + config.apiPort + '/image/download';
          const responseBody = JSON.parse(response.body);
          console.log(responseBody);
        });
      }, stompReconnect);
    }

    const stompReconnect = () => {
      setTimeout(stompConnect, 1000);
    }

    stompConnect();
  }

  test1 = () => {
    stompClient.send("/app/talk/room.find", {}, JSON.stringify({}));
  }

  test2 = () => {
    stompClient.send("/app/talk/room.insert", {}, JSON.stringify({}));
  }

  test3 = () => {
    stompClient.send("/app/talk/room.update", {}, JSON.stringify({}));
  }

  test4 = () => {
    stompClient.send("/app/talk/room.delete", {}, JSON.stringify({}));
  }

  render() {
    return (
      <div>
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <button style={{ marginTop: '5px' }} onClick={this.handleLogout}>Logout</button>
        </div>
        <section>
          <p>test</p>
          <div>
            <button onClick={this.test1}>목록</button>
            <button onClick={this.test2}>추가</button>
            <button onClick={this.test3}>수정</button>
            <button onClick={this.test4}>삭제</button>
          </div>
        </section>
      </div>
    );
  }
}

if (__DEV__) {
  // Not needed or used in minified mode
  TalkAppComponent.propTypes = {
    credentialsActions: PropTypes.shape({
      clearCredentials: PropTypes.func.isRequired,
      checkCredentials: PropTypes.func.isRequired,
      checkCredentialsSucess: PropTypes.func.isRequired,
      checkCredentialsFailure: PropTypes.func.isRequired,
      addCredentials: PropTypes.func.isRequired,
      addCredentialsSucess: PropTypes.func.isRequired,
      addCredentialsFailure: PropTypes.func.isRequired
    }).isRequired
  };
}

const TalkApp = connect(state => ({  }), dispatch => ({
  credentialsActions: bindActionCreators(CredentialsActions, dispatch),
}))(TalkAppComponent);

export default TalkApp;
