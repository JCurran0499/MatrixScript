const userAction = async (command) => {
  const response = await fetch('http://localhost:4567/', {
    credentials: 'same-origin', 
    method: 'POST',
    body: `{"command": "${command}"}`,
  });
  const myJson = await response.json(); //extract JSON from the http response
  console.log(myJson.response)
}

command = prompt("Enter command:")
userAction(command)

