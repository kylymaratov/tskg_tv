import { useEffect, useState } from 'react'

function App() {
  const [nodeVersion, setNodeVersion] = useState<string | undefined>(undefined)

  useEffect(() => {
    backend.nodeVersion('Hello, from React').then((value) => {
      setNodeVersion(value)
    })
  }, [])

  return <>Node version {nodeVersion}</>
}

export default App
