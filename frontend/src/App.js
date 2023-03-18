import { Title } from './components/title/Title'
import { Box } from './components/box/Box'
import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css';

function App() {
  const [session, handleSession] = useState('')

  useEffect(() => {
    axios({
      method: 'get',
      url: `http://${process.env.REACT_APP_BACKEND}:4567/token`
    })
    .then((res) => res.data)
    .then((json) => json.sessionToken)
    .then(handleSession)
  }, [])


  return (
    <div className="App">
      <Title/>
      <Box sessionToken={session}/>
      <div id="annotations">
        <p>&copy; Jack Curran, 2023 | <a href="https://google.com">Read the Docs</a></p>
      </div>
    </div>
  );
}

export default App;
