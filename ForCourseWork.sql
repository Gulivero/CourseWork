USE HumanResource;

CREATE TABLE "Departments" (
	[_id] INT PRIMARY KEY IDENTITY(1,1),
	[name_of_department] NVARCHAR(50) NOT NULL UNIQUE
)

CREATE TABLE "Workers" (
	[_id]	INT PRIMARY KEY IDENTITY(1,1),
	[login]	nvarchar(50) NOT NULL UNIQUE,
	[password]	nvarchar(50) NOT NULL UNIQUE,
	[name]	nvarchar(50) NOT NULL,
	[surname]	nvarchar(50) NOT NULL,
	[sex]	nvarchar(50) NOT NULL CHECK(sex in ('М','Ж')),
	[department] nvarchar(50) NOT NULL,
	[position] nvarchar(50) NOT NULL,
	[salt] varchar(250)
	FOREIGN KEY("department") REFERENCES "Departments"("name_of_department") ON UPDATE CASCADE
);

INSERT INTO [dbo].[Workers] 
			([login]
           ,[password]
           ,[name]
           ,[surname]
           ,[sex]
           ,[department]
           ,[position])
	VALUES
           ('admin',
		    'admin',
			'admin',
			'admin',
			'М',
			'Администраторы',
			'Администратор')