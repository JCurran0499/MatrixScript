const userAction = async () => {
  const response = await fetch('http://localhost:4567/', { 
    method: 'POST',
    body: '{"command": "2+3"}'
  });
  const myJson = await response.json(); //extract JSON from the http response
  console.log(myJson)
}

userAction()