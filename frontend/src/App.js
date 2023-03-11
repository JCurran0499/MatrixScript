import { Title } from './components/title/Title'
import { Input } from './components/input/Input'
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
    </div>
  );
}

export default App;
