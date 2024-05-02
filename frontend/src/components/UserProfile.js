import CreateForm from "./create-photo-form/CreateForm";
import { useState } from "react";

function App() {
  const [startPopup, setStartPopup] = useState(false);

  function handleStart() {
    setStartPopup(true);
  }
  function handleClose() {
    setStartPopup(false);
  }

  return (
      <div className="App">
        {startPopup ? <CreateForm handleClose={handleClose} /> : <></>}
        <button onClick={handleStart}> создавашки</button>
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus amet architecto, delectus, eveniet facere illo labore natus, nihil non officiis pariatur placeat quaerat! Ad at dolorum, eius impedit maxime odit?
      </div>
  );
}

export default App;
