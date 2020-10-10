import { saveAs } from 'file-saver';

export const CSV_EXTENSION = '.csv';
export const SQL_EXTENSION = '.sql';

export function exportFile(data: ArrayBuffer, name: string, ext: string){
	const blob = new Blob([data], { type: 'application/octet-stream' });
	const now = new Date();
	const month = (now.getMonth()+1);
	var strMonth = (month < 10 ? '0' : '') + month;
	const strNow = now.getDate()+ strMonth +now.getFullYear()+'_'+now.getHours()+''+now.getMinutes()+''+now.getSeconds();
	const fileName = name + strNow + ext;
	saveAs(blob, fileName);
}

export function ab2str(buffer) {
  return new TextDecoder("utf-8").decode(buffer);
}