import { useEffect, useState } from 'react'
import { Header } from './components/Header'

function App() {
  const [nodeVersion, setNodeVersion] = useState<string | undefined>(undefined)

  useEffect(() => {
    backend.homePage().then((value) => {
      setNodeVersion(value)
    })
  }, [])

  return (
    <>
      <div>
        <Header />
        Node version {nodeVersion}
      </div>
    </>
  )
}

export default App
