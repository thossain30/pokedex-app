import React from 'react';
import './App.css';
import SearchBar from './components/SearchBar';

function App() {
  const template = require('./assets/pokedex_entry_template_2.jpg');
  return (
    <div className="App">
      <header className="App-header relative w-screen h-screen">
        <img
          src={template}
          alt="Background"
          className="w-full h-full"
        />

        {/* Search bar on top */}
        <div
          className="absolute z-10 yellow-bg p-1 rounded-md
                    w-11/12 sm:w-64 md:w-80"
          style={{
            top: "15%",        // relative to container height
            left: "70%"        // relative to container width
          }}
        >
          <SearchBar />
        </div>
        {/* <div
          className="relative w-screen h-screen w-full h-full bg-contain bg-center bg-no-repeat"
          style={{ backgroundImage: `url(${template})` }}
        >
          <div className="mt-10 ml-180 yellow-bg p-2 rounded-md w-64">
            <SearchBar />
          </div>
        </div> */}
      </header>
    </div>
  );
}

export default App;
