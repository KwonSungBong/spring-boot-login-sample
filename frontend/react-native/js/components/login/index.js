import React, { Component } from "react";
import { Image } from "react-native";
import { connect } from "react-redux";
import {
  Container,
  Content,
  Item,
  Input,
  Button,
  Icon,
  View,
  Text
} from "native-base";
import { Field, reduxForm } from "redux-form";
import { setUser } from "../../actions/user";
import styles from "./styles";

const background = require("../../../images/shadow.png");

let Socket = require('sockjs-client');
// let Stomp = require('stompjs');
// let socket;
// let stompClient;

const validate = values => {
  const error = {};
  error.email = "";
  error.password = "";
  var ema = values.email;
  var pw = values.password;
  if (values.email === undefined) {
    ema = "";
  }
  if (values.password === undefined) {
    pw = "";
  }
  if (ema.length < 8 && ema !== "") {
    error.email = "too short";
  }
  if (!ema.includes("@") && ema !== "") {
    error.email = "@ not included";
  }
  if (pw.length > 12) {
    error.password = "max 11 characters";
  }
  if (pw.length < 5 && pw.length > 0) {
    error.password = "Weak";
  }
  return error;
};

class Login extends Component {
  static propTypes = {
    setUser: React.PropTypes.func
  };
  constructor(props) {
    super(props);
    this.state = {
      name: ""
    };
    this.renderInput = this.renderInput.bind(this);
  }

  testtest() {
      const socketUrl = "http://localhost:8888/websocket";
      const stompConnect = () => {
          console.log(Socket);
          socket = new Socket(socketUrl);
          // stompClient = Stomp.over(socket);
          // // stompClient.debug = null;
          // stompClient.connect({}, function (frame) {
          //     console.log('Connected: ' + frame);
          //     const url = '/talk/room.list';
          //     stompClient.subscribe(url, function (response) {
          //         // const url = 'http://' + config.apiHost + ':' + config.apiPort + '/image/download';
          //         const responseBody = JSON.parse(response.body);
          //         console.log(responseBody);
          //     });
          // }, stompReconnect);
      }

      const stompReconnect = () => {
          setTimeout(stompConnect, 1000);
      }

      stompConnect();
  }
  test() {
      // https://facebook.github.io/react-native/docs/network.html
      fetch('http://localhost:8888/auth', {
          method: 'POST',
          headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
          },
          body: JSON.stringify({
              username: 'lynas',
              password: '123456'
          })
      })
      .then((response) => response.json())
      .then((responseJson) => {
          console.log(responseJson);
          return responseJson;
      })
      .catch((error) => {
          console.error("er", error);
      });
  }
  setUser(name) {
    this.props.setUser(name);
  }
  renderInput({
    input,
    label,
    type,
    meta: { touched, error, warning },
    inputProps
  }) {
    var hasError = false;
    if (error !== undefined) {
      hasError = true;
    }
    return (
      <Item error={hasError}>
        <Icon active name={input.name === "email" ? "person" : "unlock"} />
        <Input
          placeholder={input.name === "email" ? "EMAIL" : "PASSWORD"}
          {...input}
        />
        {hasError
          ? <Item style={{ borderColor: "transparent" }}>
              <Icon active style={{ color: "red", marginTop: 5 }} name="bug" />
              <Text style={{ fontSize: 15, color: "red" }}>{error}</Text>
            </Item>
          : <Text />}
      </Item>
    );
  }
  render() {
    return (
      <Container>
        <View style={styles.container}>
          <Content>
            <Image source={background} style={styles.shadow}>
              <View style={styles.bg}>
                <Field name="email" component={this.renderInput} />
                <Field name="password" component={this.renderInput} />
                <Button
                  style={styles.btn}
                  onPress={() => this.props.navigation.navigate("Home")}
                >
                  <Text>Login</Text>
                </Button>
                <Button
                    style={styles.btn}
                    onPress={() => this.test()}
                >
                  <Text>Test</Text>
                </Button>
                  <Button
                      style={styles.btn}
                      onPress={() => this.testtest()}
                  >
                      <Text>TestTest</Text>
                  </Button>
              </View>
            </Image>
          </Content>
        </View>
      </Container>
    );
  }
}
const LoginSwag = reduxForm(
  {
    form: "test",
    validate
  },
  function bindActions(dispatch) {
    return {
      setUser: name => dispatch(setUser(name))
    };
  }
)(Login);
LoginSwag.navigationOptions = {
  header: null
};
export default LoginSwag;
