import { app, BrowserWindow } from 'electron'
import { join } from 'path'

import './api'

const isDev = process.env.DEV != undefined
const isPreview = process.env.PREVIEW != undefined

function createWindow() {
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: join(__dirname, 'preload.js'),
    },
  })

  if (isDev) {
    mainWindow.loadURL('http://localhost:5173')
    mainWindow.webContents.openDevTools()
  } else if (isPreview) {
    mainWindow.webContents.openDevTools()
    mainWindow.loadFile('dist/index.html')
  } else {
    mainWindow.loadFile('dist/index.html')
  }
}

app.whenReady().then(() => {
  createWindow()

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit()
})
