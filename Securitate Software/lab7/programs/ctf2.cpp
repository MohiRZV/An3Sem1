// read-from-console.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#define PATH_MAX_LEN 100
#define BUFFERSIZE 512

//TCHAR *ProfileDirectory = L"c:\\profiles";
TCHAR *ProfileDirectory = L"..\\profiles";


DWORD g_BytesTransferred = 0;

void DisplayError(LPTSTR lpszFunction);

VOID CALLBACK FileIOCompletionRoutine(
	__in  DWORD dwErrorCode,
	__in  DWORD dwNumberOfBytesTransfered,
	__in  LPOVERLAPPED lpOverlapped
	);

VOID CALLBACK FileIOCompletionRoutine(
	__in  DWORD dwErrorCode,
	__in  DWORD dwNumberOfBytesTransfered,
	__in  LPOVERLAPPED lpOverlapped)
{
	UNREFERENCED_PARAMETER(lpOverlapped);

	_tprintf(TEXT("Error code:\t%x\n"), dwErrorCode);
	_tprintf(TEXT("Number of bytes:\t%x\n"), dwNumberOfBytesTransfered);
	g_BytesTransferred = dwNumberOfBytesTransfered;
}

//
// Note: this simplified sample assumes the file to read is an ANSI text file
// only for the purposes of output to the screen. CreateFile and ReadFile
// do not use parameters to differentiate between text and binary file types.
//

int __cdecl _tmain(int argc, TCHAR *argv[])
{
	HANDLE hFile;
	DWORD  dwBytesRead = 0;
	char   ReadBuffer[BUFFERSIZE] = { 0 };
	OVERLAPPED ol = { 0 };
	TCHAR *path, *cannonicalised_path, *crt_dir;// [PATH_MAX_LEN];

	printf("\n");
	if (argc != 2)
	{
		printf("Usage Error: Incorrect number of arguments\n\n");
		_tprintf(TEXT("Usage:\n\t%s <text_file_name>\n"), argv[0]);
		return 0;
	}

	//printf("sizemax = %d, sizeargv = %d\n", PATH_MAX_LEN*sizeof(TCHAR) - wcslen(ProfileDirectory) - wcslen(L"\\prof_.txt") - 1, wcslen(argv[1]));

	// protect from path truncation
	if (wcslen(argv[1]) > PATH_MAX_LEN - wcslen(ProfileDirectory) - wcslen(L"\\.txt") - 1) {
		printf("Path length too large\n");
		return 1;
	}

	if (NULL != wcsstr(argv[1], TEXT(".."))) {
		DisplayError(TEXT("Check filename"));
		_tprintf(TEXT("Terminal failure: path \"%s\" contains the illegal \"..\" char sequence.\n"), argv[1]);
		return 2;
	}

	path = (TCHAR*)malloc(PATH_MAX_LEN*sizeof(TCHAR));
	cannonicalised_path = (TCHAR*)malloc(PATH_MAX_LEN*sizeof(TCHAR));


	crt_dir = (TCHAR*)malloc(PATH_MAX_LEN*sizeof(TCHAR));

	if (0 == GetCurrentDirectory(PATH_MAX_LEN, crt_dir)) {
		DisplayError(TEXT("GetCurrentDirectory"));
		_tprintf(TEXT("Terminal failure: unable get the current directory.\n"));
		return 2;
	}

	_snwprintf_s(path, PATH_MAX_LEN, _TRUNCATE, L"%ls\\%ls\\%ls.txt", crt_dir, ProfileDirectory, argv[1]);
	wprintf(L"path = %s, size = %d\n", path, wcslen(path));

	GetFullPathName(path, PATH_MAX_LEN, cannonicalised_path, NULL);
	wprintf(L"c_path = %s, c_size = %d\n", cannonicalised_path, wcslen(cannonicalised_path));

	hFile = CreateFile(path,               // file to open
		GENERIC_READ,          // open for reading
		FILE_SHARE_READ,       // share for reading
		NULL,                  // default security
		OPEN_EXISTING,         // existing file only
		FILE_ATTRIBUTE_NORMAL | FILE_FLAG_OVERLAPPED, // normal file
		NULL);                 // no attr. template

	if (hFile == INVALID_HANDLE_VALUE)
	{
		DisplayError(TEXT("CreateFile"));
		_tprintf(TEXT("Terminal failure: unable to open file \"%s\" for read.\n"), argv[1]);
		return 2;
	}

	// Read one character less than the buffer size to save room for
	// the terminating NULL character. 

	if (FALSE == ReadFileEx(hFile, ReadBuffer, BUFFERSIZE - 1, &ol, FileIOCompletionRoutine))
	{
		DisplayError(TEXT("ReadFile"));
		printf("Terminal failure: Unable to read from file.\n GetLastError=%08x\n", GetLastError());
		CloseHandle(hFile);
		return 3;
	}
	SleepEx(50000, TRUE);
	dwBytesRead = g_BytesTransferred;
	// This is the section of code that assumes the file is ANSI text. 
	// Modify this block for other data types if needed.

	if (dwBytesRead > 0 && dwBytesRead <= BUFFERSIZE - 1)
	{
		ReadBuffer[dwBytesRead] = '\0'; // NULL character

		if (ReadBuffer[dwBytesRead - 1] == 10 && ReadBuffer[dwBytesRead - 2] == 13)
			ReadBuffer[dwBytesRead-2] = '\0';

		_tprintf(TEXT("Data read from %s (%d bytes): \n"), argv[1], dwBytesRead);
		printf("%s\n", ReadBuffer);
	}
	else if (dwBytesRead == 0)
	{
		_tprintf(TEXT("No data read from file %s\n"), argv[1]);
	}
	else
	{
		printf("\n ** Unexpected value for dwBytesRead ** \n");
	}

	//_snwprintf_s(path, PATH_MAX_LEN, _TRUNCATE, L"%s", ReadBuffer);
	//mbstowcs_s(&convertedChars, path, PATH_MAX_LEN, ReadBuffer, _TRUNCATE);
	//if (MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, ReadBuffer, -1, path, PATH_MAX_LEN) == 0) {
	//	DisplayError(TEXT("MultiByteToWideChar"));
	//	_tprintf(TEXT("Terminal failure: unable to convert string \"%hs\" to TCHAR.\n"), ReadBuffer);
	//	return 2;
	//}
	//_tprintf(TEXT("path = %s, size = %d\n"), path, wcslen(path));
	hFile = CreateFileA(ReadBuffer,               // file to open
		GENERIC_READ,          // open for reading
		FILE_SHARE_READ,       // share for reading
		NULL,                  // default security
		OPEN_EXISTING,         // existing file only
		FILE_ATTRIBUTE_NORMAL | FILE_FLAG_OVERLAPPED, // normal file
		NULL);                 // no attr. template

	if (hFile == INVALID_HANDLE_VALUE)
	{
		DisplayError(TEXT("CreateFile"));
		_tprintf(TEXT("Terminal failure: unable to open file \"%s\" for read.\n"), argv[1]);
		return 2;
	}

	if (FALSE == ReadFileEx(hFile, ReadBuffer, BUFFERSIZE - 1, &ol, FileIOCompletionRoutine))
	{
		DisplayError(TEXT("ReadFile"));
		printf("Terminal failure: Unable to read from file.\n GetLastError=%08x\n", GetLastError());
		CloseHandle(hFile);
		return 3;
	}
	SleepEx(50000, TRUE);
	dwBytesRead = g_BytesTransferred;
	// This is the section of code that assumes the file is ANSI text. 
	// Modify this block for other data types if needed.

	if (dwBytesRead > 0 && dwBytesRead <= BUFFERSIZE - 1)
	{
		ReadBuffer[dwBytesRead] = '\0'; // NULL character

										//if (ReadBuffer[dwBytesRead - 1] == 10 && ReadBuffer[dwBytesRead - 2] == 13)
										//	ReadBuffer[dwBytesRead-2] = '\0';

		_tprintf(TEXT("Data read from %s (%d bytes): \n"), argv[1], dwBytesRead);
		printf("%s\n", ReadBuffer);
	}
	else if (dwBytesRead == 0)
	{
		_tprintf(TEXT("No data read from file %s\n"), argv[1]);
	}
	else
	{
		printf("\n ** Unexpected value for dwBytesRead ** \n");
	}
	// It is always good practice to close the open file handles even though
	// the app will exit here and clean up open handles anyway.

	CloseHandle(hFile);
}

void DisplayError(LPTSTR lpszFunction)
// Routine Description:
// Retrieve and output the system error message for the last-error code
{
	LPVOID lpMsgBuf;
	LPVOID lpDisplayBuf;
	DWORD dw = GetLastError();

	FormatMessage(
		FORMAT_MESSAGE_ALLOCATE_BUFFER |
		FORMAT_MESSAGE_FROM_SYSTEM |
		FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
		dw,
		MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
		(LPTSTR)&lpMsgBuf,
		0,
		NULL);

	lpDisplayBuf =
		(LPVOID)LocalAlloc(LMEM_ZEROINIT,
			(lstrlen((LPCTSTR)lpMsgBuf)
				+ lstrlen((LPCTSTR)lpszFunction)
				+ 40) // account for format string
			* sizeof(TCHAR));

	if (FAILED(StringCchPrintf((LPTSTR)lpDisplayBuf,
		LocalSize(lpDisplayBuf) / sizeof(TCHAR),
		TEXT("%s failed with error code %d as follows:\n%s"),
		lpszFunction,
		dw,
		lpMsgBuf)))
	{
		printf("FATAL ERROR: Unable to output error code.\n");
	}

	_tprintf(TEXT("ERROR: %s\n"), (LPCTSTR)lpDisplayBuf);

	LocalFree(lpMsgBuf);
	LocalFree(lpDisplayBuf);
}


