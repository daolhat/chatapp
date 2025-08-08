import { Route, Routes } from "react-router-dom";
import HomePage from "./components/HomePage";
import Status from "./components/Status/Status";
import StatsuViewer from "./components/Status/StatusViewer";
import Signin from "./components/Register/Signin";
import Signup from "./components/Register/Signup";
import Profile from "./components/Profile/Profile";


function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/status" element={<Status />} />
        <Route path="/status/:userId" element={<StatsuViewer />} />
        <Route path="/signin" element={<Signin />} />
        <Route path="/signup" element={<Signup />} />
        {/* <Route path="/profile" element={<Profile />} /> */}
      </Routes>
    </div>
  );
}

export default App;
