import { ipcMain, IpcMainInvokeEvent } from 'electron'

ipcMain.handle('node-version', (event: IpcMainInvokeEvent): string => {
  return process.versions.node
})

ipcMain.handle('home-page', (event: IpcMainInvokeEvent): string => {
  return 'hompage'
})
